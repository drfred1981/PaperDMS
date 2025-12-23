package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.common.event.DocumentEvent;
import fr.smartprod.paperdms.common.event.DocumentEventType;
import fr.smartprod.paperdms.common.event.DocumentServiceStatusEvent;
import fr.smartprod.paperdms.common.event.DocumentUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for publishing document events to Kafka topics.
 * Handles event creation, enrichment, and publication to appropriate topics.
 */
@Service
public class DocumentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DocumentEventPublisher.class);

    private final KafkaTemplate<String, DocumentEvent> kafkaTemplate;

    @Value("${paperdms.kafka.topic.document-events:paperdms.document.events}")
    private String documentEventsTopic;

    @Value("${paperdms.kafka.topic.service-status:paperdms.document.service-status}")
    private String serviceStatusTopic;

    @Value("${spring.application.name:documentService}")
    private String serviceName;

    public DocumentEventPublisher(KafkaTemplate<String, DocumentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish a document upload event.
     *
     * @param event The document upload event to publish
     */
    public void publishUploadEvent(DocumentUploadEvent event) {
        enrichEvent(event);
        String key = generateEventKey(event.getDocumentId(), event.getEventType());

        log.info("Publishing document upload event: eventId={}, documentId={}, eventType={}",
            event.getEventId(), event.getDocumentId(), event.getEventType());

        try {
            kafkaTemplate.send(documentEventsTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish document upload event: eventId={}, error={}",
                            event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.debug("Document upload event published successfully: eventId={}, partition={}, offset={}",
                            event.getEventId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    }
                });

        } catch (Exception e) {
            log.error("Exception while publishing document upload event: eventId={}, error={}",
                event.getEventId(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish document upload event", e);
        }
    }

    /**
     * Publish a document service status event.
     *
     * @param event The service status event to publish
     */
    public void publishServiceStatusEvent(DocumentServiceStatusEvent event) {
        enrichEvent(event);
        String key = generateEventKey(event.getDocumentId(), event.getEventType());

        log.info("Publishing service status event: eventId={}, documentId={}, serviceType={}, status={}",
            event.getEventId(), event.getDocumentId(), event.getServiceType(), event.getStatus());

        try {
            kafkaTemplate.send(serviceStatusTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish service status event: eventId={}, error={}",
                            event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.debug("Service status event published successfully: eventId={}, partition={}, offset={}",
                            event.getEventId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    }
                });

        } catch (Exception e) {
            log.error("Exception while publishing service status event: eventId={}, error={}",
                event.getEventId(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish service status event", e);
        }
    }

    /**
     * Publish a generic document event.
     *
     * @param eventType The type of event
     * @param documentId The document ID
     * @param sha256 The document SHA-256 hash
     * @param userId The user ID who triggered the event
     */
    public void publishDocumentEvent(DocumentEventType eventType, Long documentId, String sha256, String userId) {
        DocumentEvent event = new DocumentEvent(eventType, documentId, serviceName);
        event.setSha256(sha256);
        event.setUserId(userId);

        enrichEvent(event);
        String key = generateEventKey(documentId, eventType);

        log.info("Publishing document event: eventId={}, documentId={}, eventType={}",
            event.getEventId(), documentId, eventType);

        try {
            kafkaTemplate.send(documentEventsTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish document event: eventId={}, error={}",
                            event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.debug("Document event published successfully: eventId={}, partition={}, offset={}",
                            event.getEventId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    }
                });

        } catch (Exception e) {
            log.error("Exception while publishing document event: eventId={}, error={}",
                event.getEventId(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish document event", e);
        }
    }

    /**
     * Enrich an event with common fields if not already set.
     *
     * @param event The event to enrich
     */
    private void enrichEvent(DocumentEvent event) {
        if (event.getEventId() == null) {
            event.setEventId(UUID.randomUUID().toString());
        }
        if (event.getSourceService() == null) {
            event.setSourceService(serviceName);
        }
        if (event.getTimestamp() == null) {
            event.setTimestamp(java.time.Instant.now());
        }
    }

    /**
     * Generate a Kafka message key for event partitioning.
     * Uses documentId to ensure all events for a document go to the same partition.
     *
     * @param documentId The document ID
     * @param eventType The event type
     * @return The generated key
     */
    private String generateEventKey(Long documentId, DocumentEventType eventType) {
        return String.format("%d-%s", documentId, eventType.name());
    }
}
