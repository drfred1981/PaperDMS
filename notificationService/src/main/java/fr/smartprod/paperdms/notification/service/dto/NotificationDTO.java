package fr.smartprod.paperdms.notification.service.dto;

import fr.smartprod.paperdms.notification.domain.enumeration.NotificationChannel;
import fr.smartprod.paperdms.notification.domain.enumeration.NotificationPriority;
import fr.smartprod.paperdms.notification.domain.enumeration.NotificationType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.notification.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    private String message;

    private NotificationType type;

    private NotificationPriority priority;

    @NotNull
    @Size(max = 50)
    private String recipientId;

    @NotNull
    private Boolean isRead;

    private Instant readDate;

    private NotificationChannel channel;

    @Size(max = 100)
    private String relatedEntityType;

    private Long relatedEntityId;

    @Size(max = 500)
    private String actionUrl;

    @Lob
    private String metadata;

    private Instant expirationDate;

    @NotNull
    private Instant sentDate;

    @NotNull
    private Instant createdDate;

    private NotificationTemplateDTO template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Instant getReadDate() {
        return readDate;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Instant getSentDate() {
        return sentDate;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public NotificationTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplateDTO template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
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
            ", template=" + getTemplate() +
            "}";
    }
}
