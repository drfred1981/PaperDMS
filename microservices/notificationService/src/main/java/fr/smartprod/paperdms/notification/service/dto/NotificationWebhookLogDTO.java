package fr.smartprod.paperdms.notification.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationWebhookLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String eventType;

    @Lob
    private String payload;

    private Integer responseStatus;

    @Lob
    private String responseBody;

    private Long responseTime;

    private Integer attemptNumber;

    @NotNull
    private Boolean isSuccess;

    @Lob
    private String errorMessage;

    @NotNull
    private Instant sentDate;

    private NotificationWebhookSubscriptionDTO subscription;

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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getSentDate() {
        return sentDate;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public NotificationWebhookSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(NotificationWebhookSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationWebhookLogDTO)) {
            return false;
        }

        NotificationWebhookLogDTO notificationWebhookLogDTO = (NotificationWebhookLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationWebhookLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationWebhookLogDTO{" +
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
            ", subscription=" + getSubscription() +
            "}";
    }
}
