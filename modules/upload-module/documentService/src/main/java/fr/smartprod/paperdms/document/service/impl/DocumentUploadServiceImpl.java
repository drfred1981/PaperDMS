package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.common.event.DocumentEventType;
import fr.smartprod.paperdms.common.event.DocumentUploadEvent;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import fr.smartprod.paperdms.document.repository.DocumentRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * Service for handling document upload operations.
 * Implements idempotent upload with SHA-256 deduplication and Kafka event publishing.
 */
@Service
@Transactional
public class DocumentUploadService {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadService.class);

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final S3StorageService s3StorageService;
    private final DocumentEventPublisher documentEventPublisher;
    private final DocumentServiceStatusService documentServiceStatusService;

    public DocumentUploadService(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        S3StorageService s3StorageService,
        DocumentEventPublisher documentEventPublisher,
        DocumentServiceStatusService documentServiceStatusService
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.s3StorageService = s3StorageService;
        this.documentEventPublisher = documentEventPublisher;
        this.documentServiceStatusService = documentServiceStatusService;
    }

    /**
     * Upload a document file with idempotent deduplication.
     * Returns existing document if SHA-256 hash matches.
     *
     * @param file The multipart file to upload
     * @param folderId Optional folder ID
     * @param documentTypeId The document type ID
     * @param userId The user ID performing the upload
     * @return The uploaded or existing DocumentDTO
     */
    public DocumentDTO uploadDocument(
        MultipartFile file,
        Long folderId,
        Long documentTypeId,
        String userId
    ) throws IOException {
        log.info("Starting document upload: fileName={}, size={}, folderId={}, documentTypeId={}, userId={}",
            file.getOriginalFilename(), file.getSize(), folderId, documentTypeId, userId);

        validateFile(file);

        byte[] fileData = file.getBytes();
        String sha256 = s3StorageService.calculateSha256(fileData);

        log.debug("File SHA-256 calculated: sha256={}", sha256);

        Optional<Document> existingDocument = documentRepository.findBySha256(sha256);
        if (existingDocument.isPresent()) {
            log.info("Document with same SHA-256 already exists (deduplication): documentId={}, sha256={}",
                existingDocument.get().getId(), sha256);
            return documentMapper.toDto(existingDocument.get());
        }

        Document document = createDocument(file, sha256, folderId, documentTypeId, userId);

        publishUploadStartedEvent(document, userId);

        try {
            S3StorageService.S3UploadResult uploadResult = s3StorageService.uploadFile(
                fileData,
                sha256,
                file.getOriginalFilename(),
                file.getContentType()
            );

            updateDocumentWithS3Info(document, uploadResult);

            Integer pageCount = extractPageCount(fileData, file.getContentType());
            document.setPageCount(pageCount);

            Document savedDocument = documentRepository.save(document);
            
            log.info("Document saved successfully: documentId={}, sha256={}", savedDocument.getId(), sha256);

            publishUploadCompletedEvent(savedDocument, userId);

            updateServiceStatus(savedDocument.getId(), ServiceStatus.COMPLETED, userId);

            return documentMapper.toDto(savedDocument);

        } catch (Exception e) {
            log.error("Document upload failed: fileName={}, sha256={}, error={}",
                file.getOriginalFilename(), sha256, e.getMessage(), e);

            if (document.getId() != null) {
                updateServiceStatus(document.getId(), ServiceStatus.FAILED, userId);
                documentEventPublisher.publishDocumentEvent(
                    DocumentEventType.DOCUMENT_UPLOAD_FAILED,
                    document.getId(),
                    sha256,
                    userId
                );
            }

            throw new RuntimeException("Document upload failed", e);
        }
    }

    /**
     * Validate uploaded file.
     *
     * @param file The file to validate
     */
    private void validateFile(MultipartFile file) {
        log.debug("Validating file: fileName={}", file.getOriginalFilename());

        if (file.isEmpty()) {
            log.error("File is empty: fileName={}", file.getOriginalFilename());
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            log.error("File name is missing");
            throw new IllegalArgumentException("File name is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            log.error("Invalid file type: contentType={}", contentType);
            throw new IllegalArgumentException("Only PDF files are supported");
        }

        log.debug("File validation passed: fileName={}", file.getOriginalFilename());
    }

    /**
     * Create a new Document entity from uploaded file.
     *
     * @param file The uploaded file
     * @param sha256 The SHA-256 hash
     * @param folderId Optional folder ID
     * @param documentTypeId The document type ID
     * @param userId The user ID
     * @return The created Document entity
     */
    private Document createDocument(
        MultipartFile file,
        String sha256,
        Long folderId,
        Long documentTypeId,
        String userId
    ) {
        log.debug("Creating document entity: fileName={}, sha256={}", file.getOriginalFilename(), sha256);

        Document document = new Document();
        document.setTitle(file.getOriginalFilename());
        document.setFileName(file.getOriginalFilename());
        document.setFileSize(file.getSize());
        document.setMimeType(file.getContentType());
        document.setSha256(sha256);
        document.setUploadDate(Instant.now());
        document.setIsPublic(false);
        document.setDownloadCount(0);
        document.setViewCount(0);
        document.setCreatedDate(Instant.now());
        document.setCreatedBy(userId);

        return document;
    }

    /**
     * Update document with S3 upload information.
     *
     * @param document The document to update
     * @param uploadResult The S3 upload result
     */
    private void updateDocumentWithS3Info(Document document, S3StorageService.S3UploadResult uploadResult) {
        log.debug("Updating document with S3 info: s3Key={}", uploadResult.getS3Key());

        document.setS3Key(uploadResult.getS3Key());
        document.setS3Bucket(uploadResult.getS3Bucket());
        document.setS3Region(uploadResult.getS3Region());
        document.setS3Etag(uploadResult.getEtag());
    }

    /**
     * Extract page count from PDF file.
     *
     * @param fileData The file data
     * @param contentType The content type
     * @return The page count, or null if not a PDF
     */
    private Integer extractPageCount(byte[] fileData, String contentType) {
        if (!"application/pdf".equals(contentType)) {
            return null;
        }

        log.debug("Extracting page count from PDF");

        try (PDDocument pdfDocument = PDDocument.load(fileData)) {
            int pageCount = pdfDocument.getNumberOfPages();
            log.debug("PDF page count extracted: pageCount={}", pageCount);
            return pageCount;
        } catch (IOException e) {
            log.warn("Failed to extract page count from PDF: error={}", e.getMessage());
            return null;
        }
    }

    /**
     * Publish upload started event.
     *
     * @param document The document being uploaded
     * @param userId The user ID
     */
    private void publishUploadStartedEvent(Document document, String userId) {
        log.debug("Publishing upload started event: sha256={}", document.getSha256());

        DocumentUploadEvent event = new DocumentUploadEvent(
            DocumentEventType.DOCUMENT_UPLOAD_STARTED,
            document.getId(),
            "documentService"
        );

        event.setFileName(document.getFileName());
        event.setFileSize(document.getFileSize());
        event.setMimeType(document.getMimeType());
        event.setSha256(document.getSha256());
        event.setUserId(userId);

        documentEventPublisher.publishUploadEvent(event);
    }

    /**
     * Publish upload completed event.
     *
     * @param document The uploaded document
     * @param userId The user ID
     */
    private void publishUploadCompletedEvent(Document document, String userId) {
        log.debug("Publishing upload completed event: documentId={}", document.getId());

        DocumentUploadEvent event = new DocumentUploadEvent(
            DocumentEventType.DOCUMENT_UPLOADED,
            document.getId(),
            "documentService"
        );

        event.setFileName(document.getFileName());
        event.setFileSize(document.getFileSize());
        event.setMimeType(document.getMimeType());
        event.setSha256(document.getSha256());
        event.setS3Key(document.getS3Key());
        event.setS3Bucket(document.getS3Bucket());
        event.setS3Region(document.getS3Region());
        event.setS3Etag(document.getS3Etag());
        event.setPageCount(document.getPageCount());
        event.setUserId(userId);

        documentEventPublisher.publishUploadEvent(event);

        documentEventPublisher.publishDocumentEvent(
            DocumentEventType.DOCUMENT_READY_FOR_OCR,
            document.getId(),
            document.getSha256(),
            userId
        );
    }

    /**
     * Update document service status.
     *
     * @param documentId The document ID
     * @param status The service status
     * @param userId The user ID
     */
    private void updateServiceStatus(Long documentId, ServiceStatus status, String userId) {
        log.debug("Updating service status: documentId={}, status={}", documentId, status);

        documentServiceStatusService.updateServiceStatus(
            documentId,
            ServiceType.OCR_SERVICE,
            status == ServiceStatus.COMPLETED ? ServiceStatus.PENDING : status,
            status == ServiceStatus.COMPLETED ? "Ready for OCR processing" : "Upload failed",
            userId
        );
    }
}
