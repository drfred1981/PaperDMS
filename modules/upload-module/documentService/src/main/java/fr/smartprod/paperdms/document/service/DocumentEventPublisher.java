package fr.smartprod.paperdms.document.service;

import org.springframework.stereotype.Service;

import fr.smartprod.paperdms.common.event.DocumentEventType;
import fr.smartprod.paperdms.common.event.DocumentServiceStatusEvent;
import fr.smartprod.paperdms.common.event.DocumentUploadEvent;

/**
 * Service for publishing document events to Kafka topics. Handles event
 * creation, enrichment, and publication to appropriate topics.
 */
@Service
public interface DocumentEventPublisher {

	/**
	 * Publish a document upload event.
	 *
	 * @param event The document upload event to publish
	 */
	public void publishUploadEvent(DocumentUploadEvent event);

	/**
	 * Publish a document service status event.
	 *
	 * @param event The service status event to publish
	 */
	public void publishServiceStatusEvent(DocumentServiceStatusEvent event);

	/**
	 * Publish a generic document event.
	 *
	 * @param eventType  The type of event
	 * @param documentId The document ID
	 * @param sha256     The document SHA-256 hash
	 * @param userId     The user ID who triggered the event
	 */
	public void publishDocumentEvent(DocumentEventType eventType, Long documentId, String sha256, String userId);
}
