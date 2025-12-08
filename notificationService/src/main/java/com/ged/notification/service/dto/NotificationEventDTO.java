package com.ged.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.notification.domain.NotificationEvent} entity.
 */
@Schema(description = "�v�nements syst�me")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationEventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String eventType;

    @Size(max = 100)
    private String entityType;

    private Long entityId;

    @Size(max = 50)
    private String userId;

    @Lob
    private String eventData;

    @NotNull
    private Instant eventDate;

    @NotNull
    private Boolean processed;

    private Instant processedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Instant getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationEventDTO)) {
            return false;
        }

        NotificationEventDTO notificationEventDTO = (NotificationEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationEventDTO{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityId=" + getEntityId() +
            ", userId='" + getUserId() + "'" +
            ", eventData='" + getEventData() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", processed='" + getProcessed() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            "}";
    }
}
