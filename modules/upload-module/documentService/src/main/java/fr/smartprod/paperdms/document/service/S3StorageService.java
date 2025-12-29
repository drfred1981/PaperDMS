package fr.smartprod.paperdms.document.service;

import org.springframework.stereotype.Service;

/**
 * Service for managing file storage operations with AWS S3. Handles upload,
 * download, deletion, and SHA-256 deduplication.
 */
@Service
public interface S3StorageService {

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
	public S3UploadResult uploadFile(byte[] fileData, String sha256, String fileName, String mimeType);

	/**
	 * Check if a file exists in S3.
	 *
	 * @param s3Key The S3 key to check
	 * @return true if the file exists, false otherwise
	 */
	public boolean fileExists(String s3Key);

	/**
	 * Get information about an existing file in S3.
	 *
	 * @param s3Key The S3 key
	 * @return S3UploadResult with the existing file information
	 */
	public S3UploadResult getExistingFileInfo(String s3Key);

	/**
	 * Delete a file from S3.
	 *
	 * @param s3Key The S3 key to delete
	 */
	public void deleteFile(String s3Key);

	/**
	 * Download a file from S3.
	 *
	 * @param s3Key The S3 key to download
	 * @return The file data as byte array
	 */
	public byte[] downloadFile(String s3Key);

	/**
	 * Calculate SHA-256 hash of file data.
	 *
	 * @param data The file data
	 * @return The SHA-256 hash as hexadecimal string
	 */
	public String calculateSha256(byte[] data);

	/**
	 * Result of an S3 upload operation.
	 */
	public static class S3UploadResult {
		private final String s3Key;
		private final String s3Bucket;
		private final String s3Region;
		private final String etag;
		private final Long fileSize;

		public S3UploadResult(String s3Key, String s3Bucket, String s3Region, String etag, Long fileSize) {
			this.s3Key = s3Key;
			this.s3Bucket = s3Bucket;
			this.s3Region = s3Region;
			this.etag = etag;
			this.fileSize = fileSize;
		}

		public String getS3Key() {
			return s3Key;
		}

		public String getS3Bucket() {
			return s3Bucket;
		}

		public String getS3Region() {
			return s3Region;
		}

		public String getEtag() {
			return etag;
		}

		public Long getFileSize() {
			return fileSize;
		}
	}
}
