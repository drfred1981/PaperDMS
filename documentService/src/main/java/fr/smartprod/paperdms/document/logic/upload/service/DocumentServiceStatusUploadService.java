package fr.smartprod.paperdms.document.logic.upload.service;

import java.util.Optional;

import fr.smartprod.paperdms.common.enumeration.ServiceStatus;
import fr.smartprod.paperdms.common.enumeration.ServiceType;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;

public interface DocumentServiceStatusUploadService {

	/**
	 * Update service status for a document with database-level locking. This method
	 * is idempotent and thread-safe.
	 *
	 * @param documentId    The document ID
	 * @param serviceType   The service type
	 * @param status        The new status
	 * @param statusDetails Optional status details
	 * @param userId        The user who triggered the update
	 * @return The updated DocumentServiceStatusDTO
	 */
	public DocumentServiceStatusDTO updateServiceStatus(Long documentId, ServiceType serviceType, ServiceStatus status,
			String statusDetails, String userId);

	/**
	 * Update service status with error information.
	 *
	 * @param documentId   The document ID
	 * @param serviceType  The service type
	 * @param errorMessage The error message
	 * @param userId       The user ID
	 * @return The updated DocumentServiceStatusDTO
	 */
	public DocumentServiceStatusDTO updateServiceStatusWithError(Long documentId, ServiceType serviceType,
			String errorMessage, String userId);

	/**
	 * Mark service processing as started.
	 *
	 * @param documentId  The document ID
	 * @param serviceType The service type
	 * @param jobId       Optional job ID
	 * @param userId      The user ID
	 * @return The updated DocumentServiceStatusDTO
	 */
	public DocumentServiceStatusDTO markProcessingStarted(Long documentId, ServiceType serviceType, String jobId,
			String userId);

	/**
	 * Mark service processing as completed.
	 *
	 * @param documentId    The document ID
	 * @param serviceType   The service type
	 * @param statusDetails Optional completion details
	 * @param userId        The user ID
	 * @return The updated DocumentServiceStatusDTO
	 */
	public DocumentServiceStatusDTO markProcessingCompleted(Long documentId, ServiceType serviceType,
			String statusDetails, String userId);

	/**
	 * Get service status for a document and service type.
	 *
	 * @param documentId  The document ID
	 * @param serviceType The service type
	 * @return Optional DocumentServiceStatusDTO
	 */

	public Optional<DocumentServiceStatusDTO> getServiceStatus(Long documentId, ServiceType serviceType);
}
