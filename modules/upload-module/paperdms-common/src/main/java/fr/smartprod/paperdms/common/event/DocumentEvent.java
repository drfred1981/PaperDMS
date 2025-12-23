package fr.smartprod.paperdms.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base event for all document-related Kafka messages.
 * This class represents the common structure for events published across the PaperDMS ecosystem.
 */
public class DocumentEvent {
    
    @JsonProperty("eventId")
    private String eventId;
    
    @JsonProperty("eventType")
    private DocumentEventType eventType;
    
    @JsonProperty("documentId")
    private Long documentId;
    
    @JsonProperty("sha256")
    private String sha256;
    
    @JsonProperty("timestamp")
    private Instant timestamp;
    
    @JsonProperty("sourceService")
    private String sourceService;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    /**
     * Default constructor required for JSON deserialization.
     */
    public DocumentEvent() {
        this.metadata = new HashMap<>();
        this.timestamp = Instant.now();
    }

    /**
     * Constructor with required fields.
     *
     * @param eventType The type of document event
     * @param documentId The ID of the document
     * @param sourceService The service that generated this event
     */
    public DocumentEvent(DocumentEventType eventType, Long documentId, String sourceService) {
        this();
        this.eventType = eventType;
        this.documentId = documentId;
        this.sourceService = sourceService;
    }

    /**
     * Add metadata to the event.
     *
     * @param key Metadata key
     * @param value Metadata value
     * @return This event instance for method chaining
     */
    public DocumentEvent addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
        return this;
    }

    // Getters and Setters

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public DocumentEventType getEventType() {
        return eventType;
    }

    public void setEventType(DocumentEventType eventType) {
        this.eventType = eventType;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEvent that = (DocumentEvent) o;
        return Objects.equals(eventId, that.eventId) &&
               eventType == that.eventType &&
               Objects.equals(documentId, that.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventType, documentId);
    }

    @Override
    public String toString() {
        return "DocumentEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", documentId=" + documentId +
                ", sha256='" + sha256 + '\'' +
                ", timestamp=" + timestamp +
                ", sourceService='" + sourceService + '\'' +
                ", userId='" + userId + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
