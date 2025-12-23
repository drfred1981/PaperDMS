package fr.smartprod.paperdms.document.logic.upload.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import fr.smartprod.paperdms.document.logic.upload.service.impl.S3StorageServiceImpl;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Unit tests for S3StorageService. Tests file upload, deduplication, SHA-256
 * hashing, and error handling.
 */
@ExtendWith(MockitoExtension.class)
class S3StorageServiceTest {

	@Mock
	private S3Client s3Client;

	private S3StorageService s3StorageService;

	private static final String TEST_BUCKET = "test-bucket";
	private static final String TEST_REGION = "us-east-1";
	private static final String TEST_PREFIX = "documents/";
	private static final byte[] TEST_DATA = "Test PDF content".getBytes();

	@BeforeEach
	void setUp() {
		s3StorageService = new S3StorageServiceImpl(s3Client);
		ReflectionTestUtils.setField(s3StorageService, "s3Bucket", TEST_BUCKET);
		ReflectionTestUtils.setField(s3StorageService, "s3Region", TEST_REGION);
		ReflectionTestUtils.setField(s3StorageService, "s3Prefix", TEST_PREFIX);
	}

	/**
	 * Test: Calculate SHA-256 hash correctly.
	 */
	@Test
	void calculateSha256_shouldReturnCorrectHash() {
		String sha256 = s3StorageService.calculateSha256(TEST_DATA);

		assertThat(sha256).isNotNull();
		assertThat(sha256).hasSize(64);
		assertThat(sha256).matches("[a-f0-9]{64}");
	}

	/**
	 * Test: Calculate SHA-256 is idempotent.
	 */
	@Test
	void calculateSha256_shouldBeIdempotent() {
		String hash1 = s3StorageService.calculateSha256(TEST_DATA);
		String hash2 = s3StorageService.calculateSha256(TEST_DATA);

		assertThat(hash1).isEqualTo(hash2);
	}

	/**
	 * Test: Upload file successfully.
	 */
	@Test
	void uploadFile_shouldUploadSuccessfully() {
		String sha256 = s3StorageService.calculateSha256(TEST_DATA);
		String fileName = "test.pdf";
		String mimeType = "application/pdf";

		when(s3Client.headObject(any(HeadObjectRequest.class))).thenThrow(NoSuchKeyException.builder().build());

		PutObjectResponse putResponse = PutObjectResponse.builder().eTag("test-etag").build();
		when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putResponse);

		S3StorageService.S3UploadResult result = s3StorageService.uploadFile(TEST_DATA, sha256, fileName, mimeType);

		assertThat(result).isNotNull();
		assertThat(result.getS3Bucket()).isEqualTo(TEST_BUCKET);
		assertThat(result.getS3Region()).isEqualTo(TEST_REGION);
		assertThat(result.getEtag()).isEqualTo("test-etag");
		assertThat(result.getFileSize()).isEqualTo(TEST_DATA.length);

		verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
	}

	/**
	 * Test: Skip upload if file already exists (deduplication).
	 */
	@Test
	void uploadFile_shouldSkipIfFileExists() {
		String sha256 = s3StorageService.calculateSha256(TEST_DATA);
		String fileName = "test.pdf";
		String mimeType = "application/pdf";

		HeadObjectResponse headResponse = HeadObjectResponse.builder().eTag("existing-etag")
				.contentLength((long) TEST_DATA.length).build();
		when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(headResponse);

		S3StorageService.S3UploadResult result = s3StorageService.uploadFile(TEST_DATA, sha256, fileName, mimeType);

		assertThat(result).isNotNull();
		assertThat(result.getEtag()).isEqualTo("existing-etag");

		verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
	}

	/**
	 * Test: File exists check returns true when file exists.
	 */
	@Test
	void fileExists_shouldReturnTrueWhenFileExists() {
		String s3Key = "test-key";

		when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(HeadObjectResponse.builder().build());

		boolean exists = s3StorageService.fileExists(s3Key);

		assertThat(exists).isTrue();
	}

	/**
	 * Test: File exists check returns false when file does not exist.
	 */
	@Test
	void fileExists_shouldReturnFalseWhenFileDoesNotExist() {
		String s3Key = "test-key";

		when(s3Client.headObject(any(HeadObjectRequest.class))).thenThrow(NoSuchKeyException.builder().build());

		boolean exists = s3StorageService.fileExists(s3Key);

		assertThat(exists).isFalse();
	}

	/**
	 * Test: Delete file successfully.
	 */
	@Test
	void deleteFile_shouldDeleteSuccessfully() {
		String s3Key = "test-key";

		when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(DeleteObjectResponse.builder().build());

		s3StorageService.deleteFile(s3Key);

		ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
		verify(s3Client).deleteObject(captor.capture());

		DeleteObjectRequest request = captor.getValue();
		assertThat(request.bucket()).isEqualTo(TEST_BUCKET);
		assertThat(request.key()).isEqualTo(s3Key);
	}

	/**
	 * Test: Download file successfully.
	 */
	@Test
	void downloadFile_shouldDownloadSuccessfully() {
		String s3Key = "test-key";

		when(s3Client.getObjectAsBytes(any(GetObjectRequest.class)))
				.thenReturn(software.amazon.awssdk.core.ResponseBytes.fromByteArray(GetObjectResponse.builder().build(),
						TEST_DATA));

		byte[] result = s3StorageService.downloadFile(s3Key);

		assertThat(result).isEqualTo(TEST_DATA);
	}

	/**
	 * Test: Upload throws exception on S3 error.
	 */
	@Test
	void uploadFile_shouldThrowExceptionOnS3Error() {
		String sha256 = s3StorageService.calculateSha256(TEST_DATA);
		String fileName = "test.pdf";
		String mimeType = "application/pdf";

		when(s3Client.headObject(any(HeadObjectRequest.class))).thenThrow(NoSuchKeyException.builder().build());
		when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
				.thenThrow(S3Exception.builder().message("S3 error").build());

		assertThatThrownBy(() -> s3StorageService.uploadFile(TEST_DATA, sha256, fileName, mimeType))
				.isInstanceOf(RuntimeException.class).hasMessageContaining("Failed to upload file to S3");
	}
}