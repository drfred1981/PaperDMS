package fr.smartprod.paperdms.notification.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WebhookSubscription.
 */
@Entity
@Table(name = "webhook_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebhookSubscription implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 1000)
    @Column(name = "url", length = 1000, nullable = false)
    private String url;

    @Size(max = 255)
    @Column(name = "secret", length = 255)
    private String secret;

    @Lob
    @Column(name = "events", nullable = false)
    private String events;

    @Lob
    @Column(name = "headers")
    private String headers;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "retry_delay")
    private Integer retryDelay;

    @Column(name = "last_trigger_date")
    private Instant lastTriggerDate;

    @Column(name = "last_success_date")
    private Instant lastSuccessDate;

    @Lob
    @Column(name = "last_error_message")
    private String lastErrorMessage;

    @Column(name = "failure_count")
    private Integer failureCount;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WebhookSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WebhookSubscription name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public WebhookSubscription url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return this.secret;
    }

    public WebhookSubscription secret(String secret) {
        this.setSecret(secret);
        return this;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEvents() {
        return this.events;
    }

    public WebhookSubscription events(String events) {
        this.setEvents(events);
        return this;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getHeaders() {
        return this.headers;
    }

    public WebhookSubscription headers(String headers) {
        this.setHeaders(headers);
        return this;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public WebhookSubscription isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public WebhookSubscription retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public WebhookSubscription maxRetries(Integer maxRetries) {
        this.setMaxRetries(maxRetries);
        return this;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getRetryDelay() {
        return this.retryDelay;
    }

    public WebhookSubscription retryDelay(Integer retryDelay) {
        this.setRetryDelay(retryDelay);
        return this;
    }

    public void setRetryDelay(Integer retryDelay) {
        this.retryDelay = retryDelay;
    }

    public Instant getLastTriggerDate() {
        return this.lastTriggerDate;
    }

    public WebhookSubscription lastTriggerDate(Instant lastTriggerDate) {
        this.setLastTriggerDate(lastTriggerDate);
        return this;
    }

    public void setLastTriggerDate(Instant lastTriggerDate) {
        this.lastTriggerDate = lastTriggerDate;
    }

    public Instant getLastSuccessDate() {
        return this.lastSuccessDate;
    }

    public WebhookSubscription lastSuccessDate(Instant lastSuccessDate) {
        this.setLastSuccessDate(lastSuccessDate);
        return this;
    }

    public void setLastSuccessDate(Instant lastSuccessDate) {
        this.lastSuccessDate = lastSuccessDate;
    }

    public String getLastErrorMessage() {
        return this.lastErrorMessage;
    }

    public WebhookSubscription lastErrorMessage(String lastErrorMessage) {
        this.setLastErrorMessage(lastErrorMessage);
        return this;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    public Integer getFailureCount() {
        return this.failureCount;
    }

    public WebhookSubscription failureCount(Integer failureCount) {
        this.setFailureCount(failureCount);
        return this;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WebhookSubscription createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public WebhookSubscription createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public WebhookSubscription lastModifiedDate(Instant lastModifiedDate) {
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
        if (!(o instanceof WebhookSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((WebhookSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebhookSubscription{" +
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
