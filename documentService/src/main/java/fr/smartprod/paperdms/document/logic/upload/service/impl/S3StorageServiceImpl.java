package fr.smartprod.paperdms.document.logic.upload.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.smartprod.paperdms.document.logic.upload.service.S3StorageService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Service for managing file storage operations with AWS S3. Handles upload,
 * download, deletion, and SHA-256 deduplication.
 */
@Service
public class S3StorageServiceImpl implements S3StorageService {

	private static final Logger log = LoggerFactory.getLogger(S3StorageServiceImpl.class);

	private final S3Client s3Client;

	@Value("${paperdms.s3.bucket}")
	private String s3Bucket;

	@Value("${paperdms.s3.region}")
	private String s3Region;

	@Value("${paperdms.s3.prefix:documents/}")
	private String s3Prefix;

	public S3StorageServiceImpl(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	/**
	 * Upload a file to S3 with deduplication check. Uses SHA-256 hash as the
	 * primary key to avoid duplicate storage.
	 *
	 * @param fileData The file data as byte array
	 * @param sha256   The SHA-256 hash of the file
	 * @param fileName The original file name
	 * @param mimeType The MIME type of the file
	 * @return S3UploadResult containing the S3 key, ETag, and bucket information
	 */
	@Override
	public S3UploadResult uploadFile(byte[] fileData, String sha256, String fileName, String mimeType) {
		log.debug("Uploading file to S3: fileName={}, sha256={}, size={}", fileName, sha256, fileData.length);

		String s3Key = generateS3Key(sha256, fileName);

		if (fileExists(s3Key)) {
			log.info("File already exists in S3, skipping upload: s3Key={}", s3Key);
			return getExistingFileInfo(s3Key);
		}

		try {
			PutObjectRequest putRequest = PutObjectRequest.builder().bucket(s3Bucket).key(s3Key).contentType(mimeType)
					.contentLength((long) fileData.length)
					.metadata(java.util.Map.of("sha256", sha256, "originalFileName", fileName)).build();

			PutObjectResponse response = s3Client.putObject(putRequest, RequestBody.fromBytes(fileData));

			log.info("File uploaded successfully to S3: s3Key={}, etag={}", s3Key, response.eTag());

			return new S3UploadResult(s3Key, s3Bucket, s3Region, response.eTag(), (long) fileData.length);

		} catch (S3Exception e) {
			log.error("Failed to upload file to S3: s3Key={}, error={}", s3Key, e.getMessage(), e);
			throw new RuntimeException("Failed to upload file to S3", e);
		}
	}

	/**
	 * Check if a file exists in S3.
	 *
	 * @param s3Key The S3 key to check
	 * @return true if the file exists, false otherwise
	 */
	@Override
	public boolean fileExists(String s3Key) {
		log.debug("Checking if file exists in S3: s3Key={}", s3Key);

		try {
			HeadObjectRequest headRequest = HeadObjectRequest.builder().bucket(s3Bucket).key(s3Key).build();

			s3Client.headObject(headRequest);
			log.debug("File exists in S3: s3Key={}", s3Key);
			return true;

		} catch (NoSuchKeyException e) {
			log.debug("File does not exist in S3: s3Key={}", s3Key);
			return false;
		} catch (S3Exception e) {
			log.error("Error checking file existence in S3: s3Key={}, error={}", s3Key, e.getMessage(), e);
			throw new RuntimeException("Failed to check file existence in S3", e);
		}
	}

	/**
	 * Get information about an existing file in S3.
	 *
	 * @param s3Key The S3 key
	 * @return S3UploadResult with the existing file information
	 */
	@Override
	public S3UploadResult getExistingFileInfo(String s3Key) {
		log.debug("Getting existing file info from S3: s3Key={}", s3Key);

		try {
			HeadObjectRequest headRequest = HeadObjectRequest.builder().bucket(s3Bucket).key(s3Key).build();

			HeadObjectResponse response = s3Client.headObject(headRequest);

			return new S3UploadResult(s3Key, s3Bucket, s3Region, response.eTag(), response.contentLength());

		} catch (S3Exception e) {
			log.error("Failed to get file info from S3: s3Key={}, error={}", s3Key, e.getMessage(), e);
			throw new RuntimeException("Failed to get file info from S3", e);
		}
	}

	/**
	 * Delete a file from S3.
	 *
	 * @param s3Key The S3 key to delete
	 */
	@Override
	public void deleteFile(String s3Key) {
		log.debug("Deleting file from S3: s3Key={}", s3Key);

		try {
			DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(s3Bucket).key(s3Key).build();

			s3Client.deleteObject(deleteRequest);
			log.info("File deleted successfully from S3: s3Key={}", s3Key);

		} catch (S3Exception e) {
			log.error("Failed to delete file from S3: s3Key={}, error={}", s3Key, e.getMessage(), e);
			throw new RuntimeException("Failed to delete file from S3", e);
		}
	}

	/**
	 * Download a file from S3.
	 *
	 * @param s3Key The S3 key to download
	 * @return The file data as byte array
	 */
	@Override
	public byte[] downloadFile(String s3Key) {
		log.debug("Downloading file from S3: s3Key={}", s3Key);

		try {
			GetObjectRequest getRequest = GetObjectRequest.builder().bucket(s3Bucket).key(s3Key).build();

			byte[] data = s3Client.getObjectAsBytes(getRequest).asByteArray();
			log.info("File downloaded successfully from S3: s3Key={}, size={}", s3Key, data.length);
			return data;

		} catch (S3Exception e) {
			log.error("Failed to download file from S3: s3Key={}, error={}", s3Key, e.getMessage(), e);
			throw new RuntimeException("Failed to download file from S3", e);
		}
	}

	/**
	 * Calculate SHA-256 hash of file data.
	 *
	 * @param data The file data
	 * @return The SHA-256 hash as hexadecimal string
	 */
	@Override
	public String calculateSha256(byte[] data) {
		log.debug("Calculating SHA-256 hash for data of size: {}", data.length);

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data);
			String sha256 = HexFormat.of().formatHex(hash);
			log.debug("SHA-256 calculated: {}", sha256);
			return sha256;

		} catch (NoSuchAlgorithmException e) {
			log.error("SHA-256 algorithm not available", e);
			throw new RuntimeException("SHA-256 algorithm not available", e);
		}
	}

	/**
	 * Generate S3 key from SHA-256 hash and file name. Format: documents/[first 2
	 * chars of hash]/[next 2 chars]/[full hash].[extension]
	 *
	 * @param sha256   The SHA-256 hash
	 * @param fileName The original file name
	 * @return The generated S3 key
	 */
	private String generateS3Key(String sha256, String fileName) {
		String extension = "";
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			extension = fileName.substring(dotIndex);
		}

		return String.format("%s%s/%s/%s%s", s3Prefix, sha256.substring(0, 2), sha256.substring(2, 4), sha256,
				extension);
	}

}
