package com.ged.notification.domain;

import com.ged.notification.domain.enumeration.NotificationChannel;
import com.ged.notification.domain.enumeration.NotificationPriority;
import com.ged.notification.domain.enumeration.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Notification utilisateur
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private NotificationPriority priority;

    @NotNull
    @Size(max = 50)
    @Column(name = "recipient_id", length = 50, nullable = false)
    private String recipientId;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "read_date")
    private Instant readDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @Size(max = 100)
    @Column(name = "related_entity_type", length = 100)
    private String relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Size(max = 500)
    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @NotNull
    @Column(name = "sent_date", nullable = false)
    private Instant sentDate;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private NotificationTemplate template;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationPriority getPriority() {
        return this.priority;
    }

    public Notification priority(NotificationPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public String getRecipientId() {
        return this.recipientId;
    }

    public Notification recipientId(String recipientId) {
        this.setRecipientId(recipientId);
        return this;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public Notification isRead(Boolean isRead) {
        this.setIsRead(isRead);
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public Notification readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public NotificationChannel getChannel() {
        return this.channel;
    }

    public Notification channel(NotificationChannel channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getRelatedEntityType() {
        return this.relatedEntityType;
    }

    public Notification relatedEntityType(String relatedEntityType) {
        this.setRelatedEntityType(relatedEntityType);
        return this;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public Long getRelatedEntityId() {
        return this.relatedEntityId;
    }

    public Notification relatedEntityId(Long relatedEntityId) {
        this.setRelatedEntityId(relatedEntityId);
        return this;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public Notification actionUrl(String actionUrl) {
        this.setActionUrl(actionUrl);
        return this;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Notification metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public Notification expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Instant getSentDate() {
        return this.sentDate;
    }

    public Notification sentDate(Instant sentDate) {
        this.setSentDate(sentDate);
        return this;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Notification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public NotificationTemplate getTemplate() {
        return this.template;
    }

    public void setTemplate(NotificationTemplate notificationTemplate) {
        this.template = notificationTemplate;
    }

    public Notification template(NotificationTemplate notificationTemplate) {
        this.setTemplate(notificationTemplate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", type='" + getType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", recipientId='" + getRecipientId() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", channel='" + getChannel() + "'" +
            ", relatedEntityType='" + getRelatedEntityType() + "'" +
            ", relatedEntityId=" + getRelatedEntityId() +
            ", actionUrl='" + getActionUrl() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", sentDate='" + getSentDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
