package com.ged.notification.service.dto;

import com.ged.notification.domain.enumeration.NotificationChannel;
import com.ged.notification.domain.enumeration.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.notification.domain.NotificationTemplate} entity.
 */
@Schema(description = "Template de notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String subject;

    @Lob
    private String bodyTemplate;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationChannel channel;

    @Lob
    private String variables;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationTemplateDTO)) {
            return false;
        }

        NotificationTemplateDTO notificationTemplateDTO = (NotificationTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationTemplateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", subject='" + getSubject() + "'" +
            ", bodyTemplate='" + getBodyTemplate() + "'" +
            ", type='" + getType() + "'" +
            ", channel='" + getChannel() + "'" +
            ", variables='" + getVariables() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
