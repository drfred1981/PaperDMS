package fr.smartprod.paperdms.notification.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.notification.domain.WebhookSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebhookSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 1000)
    private String url;

    @Size(max = 255)
    private String secret;

    @Lob
    private String events;

    @Lob
    private String headers;

    @NotNull
    private Boolean isActive;

    private Integer retryCount;

    private Integer maxRetries;

    private Integer retryDelay;

    private Instant lastTriggerDate;

    private Instant lastSuccessDate;

    @Lob
    private String lastErrorMessage;

    private Integer failureCount;

    @NotNull
    @Size(max = 50)
    private String createdBy;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(Integer retryDelay) {
        this.retryDelay = retryDelay;
    }

    public Instant getLastTriggerDate() {
        return lastTriggerDate;
    }

    public void setLastTriggerDate(Instant lastTriggerDate) {
        this.lastTriggerDate = lastTriggerDate;
    }

    public Instant getLastSuccessDate() {
        return lastSuccessDate;
    }

    public void setLastSuccessDate(Instant lastSuccessDate) {
        this.lastSuccessDate = lastSuccessDate;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        if (!(o instanceof WebhookSubscriptionDTO)) {
            return false;
        }

        WebhookSubscriptionDTO webhookSubscriptionDTO = (WebhookSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, webhookSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebhookSubscriptionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", secret='" + getSecret() + "'" +
            ", events='" + getEvents() + "'" +
            ", headers='" + getHeaders() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", retryCount=" + getRetryCount() +
            ", maxRetries=" + getMaxRetries() +
            ", retryDelay=" + getRetryDelay() +
            ", lastTriggerDate='" + getLastTriggerDate() + "'" +
            ", lastSuccessDate='" + getLastSuccessDate() + "'" +
            ", lastErrorMessage='" + getLastErrorMessage() + "'" +
            ", failureCount=" + getFailureCount() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
