package fr.smartprod.paperdms.notification.service.dto;

import fr.smartprod.paperdms.notification.domain.enumeration.NotificationFrequency;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.notification.domain.NotificationPreference} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPreferenceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String userId;

    @NotNull
    private Boolean emailEnabled;

    @NotNull
    private Boolean pushEnabled;

    @NotNull
    private Boolean inAppEnabled;

    @Lob
    private String notificationTypes;

    @Size(max = 5)
    private String quietHoursStart;

    @Size(max = 5)
    private String quietHoursEnd;

    private NotificationFrequency frequency;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public Boolean getPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(Boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public Boolean getInAppEnabled() {
        return inAppEnabled;
    }

    public void setInAppEnabled(Boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public String getNotificationTypes() {
        return notificationTypes;
    }

    public void setNotificationTypes(String notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public String getQuietHoursStart() {
        return quietHoursStart;
    }

    public void setQuietHoursStart(String quietHoursStart) {
        this.quietHoursStart = quietHoursStart;
    }

    public String getQuietHoursEnd() {
        return quietHoursEnd;
    }

    public void setQuietHoursEnd(String quietHoursEnd) {
        this.quietHoursEnd = quietHoursEnd;
    }

    public NotificationFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(NotificationFrequency frequency) {
        this.frequency = frequency;
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
        if (!(o instanceof NotificationPreferenceDTO)) {
            return false;
        }

        NotificationPreferenceDTO notificationPreferenceDTO = (NotificationPreferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationPreferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationPreferenceDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", emailEnabled='" + getEmailEnabled() + "'" +
            ", pushEnabled='" + getPushEnabled() + "'" +
            ", inAppEnabled='" + getInAppEnabled() + "'" +
            ", notificationTypes='" + getNotificationTypes() + "'" +
            ", quietHoursStart='" + getQuietHoursStart() + "'" +
            ", quietHoursEnd='" + getQuietHoursEnd() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
