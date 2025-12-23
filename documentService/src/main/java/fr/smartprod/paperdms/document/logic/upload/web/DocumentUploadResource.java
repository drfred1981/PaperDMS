package fr.smartprod.paperdms.document.logic.upload.web;

import fr.smartprod.paperdms.document.logic.upload.service.DocumentUploadService;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for document upload operations.
 * Provides endpoints for uploading PDF documents with drag-and-drop support.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentUploadResource {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadResource.class);

    private final DocumentUploadService documentUploadService;

    public DocumentUploadResource(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    /**
     * Upload a PDF document.
     * POST /api/documents/upload
     *
     * @param file The PDF file to upload
     * @param folderId Optional folder ID
     * @param documentTypeId The document type ID
     * @param authentication The authentication context
     * @return The uploaded document DTO
     * @throws URISyntaxException if URI creation fails
     * @throws IOException if file reading fails
     */
    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "folderId", required = false) Long folderId,
        @RequestParam("documentTypeId") Long documentTypeId,
        Authentication authentication
    ) throws URISyntaxException, IOException {

        String userId = authentication.getName();

        log.info("REST request to upload document: fileName={}, folderId={}, documentTypeId={}, userId={}",
            file.getOriginalFilename(), folderId, documentTypeId, userId);

        try {
            DocumentDTO result = documentUploadService.uploadDocument(file, folderId, documentTypeId, userId);

            log.info("Document uploaded successfully: documentId={}, fileName={}",
                result.getId(), result.getFileName());

            return ResponseEntity
                .created(new URI("/api/documents/" + result.getId()))
                .body(result);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid upload request: error={}", e.getMessage());
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            log.error("Document upload failed: fileName={}, error={}",
                file.getOriginalFilename(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Upload multiple PDF documents.
     * POST /api/documents/upload/batch
     *
     * @param files The PDF files to upload
     * @param folderId Optional folder ID
     * @param documentTypeId The document type ID
     * @param authentication The authentication context
     * @return Array of uploaded document DTOs
     * @throws IOException if file reading fails
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<DocumentDTO[]> uploadDocuments(
        @RequestParam("files") MultipartFile[] files,
        @RequestParam(value = "folderId", required = false) Long folderId,
        @RequestParam("documentTypeId") Long documentTypeId,
        Authentication authentication
    ) throws IOException {

        String userId = authentication.getName();

        log.info("REST request to upload multiple documents: count={}, folderId={}, documentTypeId={}, userId={}",
            files.length, folderId, documentTypeId, userId);

        DocumentDTO[] results = new DocumentDTO[files.length];
        boolean allSucceeded = true;

        for (int i = 0; i < files.length; i++) {
            try {
                results[i] = documentUploadService.uploadDocument(files[i], folderId, documentTypeId, userId);
                log.debug("Document {} of {} uploaded successfully: documentId={}",
                    i + 1, files.length, results[i].getId());

            } catch (Exception e) {
                log.error("Failed to upload document {} of {}: fileName={}, error={}",
                    i + 1, files.length, files[i].getOriginalFilename(), e.getMessage(), e);
                allSucceeded = false;
                results[i] = null;
            }
        }

        if (allSucceeded) {
            log.info("All documents uploaded successfully: count={}", files.length);
            return ResponseEntity.ok(results);
        } else {
            log.warn("Some documents failed to upload");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(results);
        }
    }
}