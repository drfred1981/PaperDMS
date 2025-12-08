package com.ged.notification.domain;

import com.ged.notification.domain.enumeration.NotificationFrequency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Pr�f�rences de notification
 */
@Entity
@Table(name = "notification_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String userId;

    @NotNull
    @Column(name = "email_enabled", nullable = false)
    private Boolean emailEnabled;

    @NotNull
    @Column(name = "push_enabled", nullable = false)
    private Boolean pushEnabled;

    @NotNull
    @Column(name = "in_app_enabled", nullable = false)
    private Boolean inAppEnabled;

    @Lob
    @Column(name = "notification_types")
    private String notificationTypes;

    @Size(max = 5)
    @Column(name = "quiet_hours_start", length = 5)
    private String quietHoursStart;

    @Size(max = 5)
    @Column(name = "quiet_hours_end", length = 5)
    private String quietHoursEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private NotificationFrequency frequency;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationPreference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public NotificationPreference userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getEmailEnabled() {
        return this.emailEnabled;
    }

    public NotificationPreference emailEnabled(Boolean emailEnabled) {
        this.setEmailEnabled(emailEnabled);
        return this;
    }

    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public Boolean getPushEnabled() {
        return this.pushEnabled;
    }

    public NotificationPreference pushEnabled(Boolean pushEnabled) {
        this.setPushEnabled(pushEnabled);
        return this;
    }

    public void setPushEnabled(Boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public Boolean getInAppEnabled() {
        return this.inAppEnabled;
    }

    public NotificationPreference inAppEnabled(Boolean inAppEnabled) {
        this.setInAppEnabled(inAppEnabled);
        return this;
    }

    public void setInAppEnabled(Boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public String getNotificationTypes() {
        return this.notificationTypes;
    }

    public NotificationPreference notificationTypes(String notificationTypes) {
        this.setNotificationTypes(notificationTypes);
        return this;
    }

    public void setNotificationTypes(String notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public String getQuietHoursStart() {
        return this.quietHoursStart;
    }

    public NotificationPreference quietHoursStart(String quietHoursStart) {
        this.setQuietHoursStart(quietHoursStart);
        return this;
    }

    public void setQuietHoursStart(String quietHoursStart) {
        this.quietHoursStart = quietHoursStart;
    }

    public String getQuietHoursEnd() {
        return this.quietHoursEnd;
    }

    public NotificationPreference quietHoursEnd(String quietHoursEnd) {
        this.setQuietHoursEnd(quietHoursEnd);
        return this;
    }

    public void setQuietHoursEnd(String quietHoursEnd) {
        this.quietHoursEnd = quietHoursEnd;
    }

    public NotificationFrequency getFrequency() {
        return this.frequency;
    }

    public NotificationPreference frequency(NotificationFrequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(NotificationFrequency frequency) {
        this.frequency = frequency;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public NotificationPreference lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationPreference)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationPreference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationPreference{" +
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
