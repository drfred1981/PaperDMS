package com.ged.notification.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * �v�nements syst�me
 */
@Entity
@Table(name = "notification_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Size(max = 100)
    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Size(max = 50)
    @Column(name = "user_id", length = 50)
    private String userId;

    @Lob
    @Column(name = "event_data")
    private String eventData;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @NotNull
    @Column(name = "processed", nullable = false)
    private Boolean processed;

    @Column(name = "processed_date")
    private Instant processedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public NotificationEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public NotificationEvent entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public NotificationEvent entityId(Long entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUserId() {
        return this.userId;
    }

    public NotificationEvent userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventData() {
        return this.eventData;
    }

    public NotificationEvent eventData(String eventData) {
        this.setEventData(eventData);
        return this;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public Instant getEventDate() {
        return this.eventDate;
    }

    public NotificationEvent eventDate(Instant eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Boolean getProcessed() {
        return this.processed;
    }

    public NotificationEvent processed(Boolean processed) {
        this.setProcessed(processed);
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Instant getProcessedDate() {
        return this.processedDate;
    }

    public NotificationEvent processedDate(Instant processedDate) {
        this.setProcessedDate(processedDate);
        return this;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationEvent{" +
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
