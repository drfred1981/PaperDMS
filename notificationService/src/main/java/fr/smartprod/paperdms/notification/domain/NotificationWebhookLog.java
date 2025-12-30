package fr.smartprod.paperdms.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NotificationWebhookLog.
 */
@Entity
@Table(name = "notification_webhook_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationWebhookLog implements Serializable {

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

    @Lob
    @Column(name = "payload")
    private String payload;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "response_time")
    private Long responseTime;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @NotNull
    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @NotNull
    @Column(name = "sent_date", nullable = false)
    private Instant sentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notificationWebhookLogs" }, allowSetters = true)
    private NotificationWebhookSubscription subscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationWebhookLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public NotificationWebhookLog eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return this.payload;
    }

    public NotificationWebhookLog payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getResponseStatus() {
        return this.responseStatus;
    }

    public NotificationWebhookLog responseStatus(Integer responseStatus) {
        this.setResponseStatus(responseStatus);
        return this;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public NotificationWebhookLog responseBody(String responseBody) {
        this.setResponseBody(responseBody);
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Long getResponseTime() {
        return this.responseTime;
    }

    public NotificationWebhookLog responseTime(Long responseTime) {
        this.setResponseTime(responseTime);
        return this;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getAttemptNumber() {
        return this.attemptNumber;
    }

    public NotificationWebhookLog attemptNumber(Integer attemptNumber) {
        this.setAttemptNumber(attemptNumber);
        return this;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Boolean getIsSuccess() {
        return this.isSuccess;
    }

    public NotificationWebhookLog isSuccess(Boolean isSuccess) {
        this.setIsSuccess(isSuccess);
        return this;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public NotificationWebhookLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getSentDate() {
        return this.sentDate;
    }

    public NotificationWebhookLog sentDate(Instant sentDate) {
        this.setSentDate(sentDate);
        return this;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public NotificationWebhookSubscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(NotificationWebhookSubscription notificationWebhookSubscription) {
        this.subscription = notificationWebhookSubscription;
    }

    public NotificationWebhookLog subscription(NotificationWebhookSubscription notificationWebhookSubscription) {
        this.setSubscription(notificationWebhookSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationWebhookLog)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationWebhookLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationWebhookLog{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", payload='" + getPayload() + "'" +
            ", responseStatus=" + getResponseStatus() +
            ", responseBody='" + getResponseBody() + "'" +
            ", responseTime=" + getResponseTime() +
            ", attemptNumber=" + getAttemptNumber() +
            ", isSuccess='" + getIsSuccess() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sentDate='" + getSentDate() + "'" +
            "}";
    }
}
