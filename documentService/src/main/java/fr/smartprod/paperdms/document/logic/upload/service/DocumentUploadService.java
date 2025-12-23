package fr.smartprod.paperdms.document.logic.upload.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import fr.smartprod.paperdms.document.service.dto.DocumentDTO;

/**
 * Service for handling document upload operations. Implements idempotent upload
 * with SHA-256 deduplication and Kafka event publishing.
 */

public interface DocumentUploadService {

	/**
	 * Upload a document file with idempotent deduplication. Returns existing
	 * document if SHA-256 hash matches.
	 *
	 * @param file           The multipart file to upload
	 * @param folderId       Optional folder ID
	 * @param documentTypeId The document type ID
	 * @param userId         The user ID performing the upload
	 * @return The uploaded or existing DocumentDTO
	 */
	public DocumentDTO uploadDocument(MultipartFile file, Long folderId, Long documentTypeId, String userId)
			throws IOException;

}
