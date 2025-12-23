package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.repository.DocumentRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DocumentUploadService.
 * Tests upload workflow, deduplication, and event publishing.
 */
@ExtendWith(MockitoExtension.class)
class DocumentUploadServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private DocumentEventPublisher documentEventPublisher;

    @Mock
    private DocumentServiceStatusService documentServiceStatusService;

    @Mock
    private MultipartFile multipartFile;

    private DocumentUploadService documentUploadService;

    private static final byte[] TEST_DATA = "Test PDF content".getBytes();
    private static final String TEST_SHA256 = "abc123def456";
    private static final String TEST_FILENAME = "test.pdf";
    private static final String TEST_MIME_TYPE = "application/pdf";
    private static final Long TEST_FOLDER_ID = 1L;
    private static final Long TEST_DOC_TYPE_ID = 2L;
    private static final String TEST_USER_ID = "testuser";

    @BeforeEach
    void setUp() {
        documentUploadService = new DocumentUploadService(
            documentRepository,
            documentMapper,
            s3StorageService,
            documentEventPublisher,
            documentServiceStatusService
        );
    }

    /**
     * Test: Upload document successfully.
     */
    @Test
    void uploadDocument_shouldUploadSuccessfully() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn(TEST_FILENAME);
        when(multipartFile.getSize()).thenReturn((long) TEST_DATA.length);
        when(multipartFile.getContentType()).thenReturn(TEST_MIME_TYPE);
        when(multipartFile.getBytes()).thenReturn(TEST_DATA);
        when(multipartFile.isEmpty()).thenReturn(false);

        when(s3StorageService.calculateSha256(TEST_DATA)).thenReturn(TEST_SHA256);
        when(documentRepository.findBySha256(TEST_SHA256)).thenReturn(Optional.empty());

        S3StorageService.S3UploadResult uploadResult = new S3StorageService.S3UploadResult(
            "s3-key", "s3-bucket", "us-east-1", "etag", (long) TEST_DATA.length
        );
        when(s3StorageService.uploadFile(any(), any(), any(), any())).thenReturn(uploadResult);

        Document savedDocument = new Document();
        savedDocument.setId(1L);
        savedDocument.setSha256(TEST_SHA256);
        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);

        DocumentDTO expectedDto = new DocumentDTO();
        expectedDto.setId(1L);
        when(documentMapper.toDto(any(Document.class))).thenReturn(expectedDto);

        DocumentDTO result = documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        );

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(documentRepository, times(1)).save(any(Document.class));
        verify(documentEventPublisher, times(1)).publishUploadEvent(any());
        verify(documentServiceStatusService, times(1)).updateServiceStatus(any(), any(), any(), any(), any());
    }

    /**
     * Test: Return existing document if SHA-256 matches (deduplication).
     */
    @Test
    void uploadDocument_shouldReturnExistingDocumentIfSha256Matches() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn(TEST_FILENAME);
        when(multipartFile.getContentType()).thenReturn(TEST_MIME_TYPE);
        when(multipartFile.getBytes()).thenReturn(TEST_DATA);
        when(multipartFile.isEmpty()).thenReturn(false);

        when(s3StorageService.calculateSha256(TEST_DATA)).thenReturn(TEST_SHA256);

        Document existingDocument = new Document();
        existingDocument.setId(99L);
        existingDocument.setSha256(TEST_SHA256);
        when(documentRepository.findBySha256(TEST_SHA256)).thenReturn(Optional.of(existingDocument));

        DocumentDTO existingDto = new DocumentDTO();
        existingDto.setId(99L);
        when(documentMapper.toDto(existingDocument)).thenReturn(existingDto);

        DocumentDTO result = documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        );

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(99L);

        verify(documentRepository, never()).save(any(Document.class));
        verify(s3StorageService, never()).uploadFile(any(), any(), any(), any());
    }

    /**
     * Test: Reject empty file.
     */
    @Test
    void uploadDocument_shouldRejectEmptyFile() {
        when(multipartFile.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("File cannot be empty");
    }

    /**
     * Test: Reject non-PDF file.
     */
    @Test
    void uploadDocument_shouldRejectNonPdfFile() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.docx");
        when(multipartFile.getContentType()).thenReturn("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        assertThatThrownBy(() -> documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Only PDF files are supported");
    }

    /**
     * Test: Reject file without name.
     */
    @Test
    void uploadDocument_shouldRejectFileWithoutName() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(null);

        assertThatThrownBy(() -> documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("File name is required");
    }

    /**
     * Test: Publish failure event on upload error.
     */
    @Test
    void uploadDocument_shouldPublishFailureEventOnError() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn(TEST_FILENAME);
        when(multipartFile.getSize()).thenReturn((long) TEST_DATA.length);
        when(multipartFile.getContentType()).thenReturn(TEST_MIME_TYPE);
        when(multipartFile.getBytes()).thenReturn(TEST_DATA);
        when(multipartFile.isEmpty()).thenReturn(false);

        when(s3StorageService.calculateSha256(TEST_DATA)).thenReturn(TEST_SHA256);
        when(documentRepository.findBySha256(TEST_SHA256)).thenReturn(Optional.empty());
        when(s3StorageService.uploadFile(any(), any(), any(), any()))
            .thenThrow(new RuntimeException("S3 error"));

        Document document = new Document();
        document.setId(1L);
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        assertThatThrownBy(() -> documentUploadService.uploadDocument(
            multipartFile, TEST_FOLDER_ID, TEST_DOC_TYPE_ID, TEST_USER_ID
        ))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Document upload failed");

        verify(documentEventPublisher, times(1)).publishDocumentEvent(any(), any(), any(), any());
        verify(documentServiceStatusService, times(1)).updateServiceStatus(any(), any(), any(), any(), any());
    }
}
