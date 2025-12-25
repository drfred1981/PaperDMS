package fr.smartprod.paperdms.notification.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.notification.domain.WebhookSubscription} entity. This class is used
 * in {@link fr.smartprod.paperdms.notification.web.rest.WebhookSubscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /webhook-subscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebhookSubscriptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter url;

    private StringFilter secret;

    private BooleanFilter isActive;

    private IntegerFilter retryCount;

    private IntegerFilter maxRetries;

    private IntegerFilter retryDelay;

    private InstantFilter lastTriggerDate;

    private InstantFilter lastSuccessDate;

    private IntegerFilter failureCount;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public WebhookSubscriptionCriteria() {}

    public WebhookSubscriptionCriteria(WebhookSubscriptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.secret = other.optionalSecret().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.retryCount = other.optionalRetryCount().map(IntegerFilter::copy).orElse(null);
        this.maxRetries = other.optionalMaxRetries().map(IntegerFilter::copy).orElse(null);
        this.retryDelay = other.optionalRetryDelay().map(IntegerFilter::copy).orElse(null);
        this.lastTriggerDate = other.optionalLastTriggerDate().map(InstantFilter::copy).orElse(null);
        this.lastSuccessDate = other.optionalLastSuccessDate().map(InstantFilter::copy).orElse(null);
        this.failureCount = other.optionalFailureCount().map(IntegerFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WebhookSubscriptionCriteria copy() {
        return new WebhookSubscriptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getSecret() {
        return secret;
    }

    public Optional<StringFilter> optionalSecret() {
        return Optional.ofNullable(secret);
    }

    public StringFilter secret() {
        if (secret == null) {
            setSecret(new StringFilter());
        }
        return secret;
    }

    public void setSecret(StringFilter secret) {
        this.secret = secret;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getRetryCount() {
        return retryCount;
    }

    public Optional<IntegerFilter> optionalRetryCount() {
        return Optional.ofNullable(retryCount);
    }

    public IntegerFilter retryCount() {
        if (retryCount == null) {
            setRetryCount(new IntegerFilter());
        }
        return retryCount;
    }

    public void setRetryCount(IntegerFilter retryCount) {
        this.retryCount = retryCount;
    }

    public IntegerFilter getMaxRetries() {
        return maxRetries;
    }

    public Optional<IntegerFilter> optionalMaxRetries() {
        return Optional.ofNullable(maxRetries);
    }

    public IntegerFilter maxRetries() {
        if (maxRetries == null) {
            setMaxRetries(new IntegerFilter());
        }
        return maxRetries;
    }

    public void setMaxRetries(IntegerFilter maxRetries) {
        this.maxRetries = maxRetries;
    }

    public IntegerFilter getRetryDelay() {
        return retryDelay;
    }

    public Optional<IntegerFilter> optionalRetryDelay() {
        return Optional.ofNullable(retryDelay);
    }

    public IntegerFilter retryDelay() {
        if (retryDelay == null) {
            setRetryDelay(new IntegerFilter());
        }
        return retryDelay;
    }

    public void setRetryDelay(IntegerFilter retryDelay) {
        this.retryDelay = retryDelay;
    }

    public InstantFilter getLastTriggerDate() {
        return lastTriggerDate;
    }

    public Optional<InstantFilter> optionalLastTriggerDate() {
        return Optional.ofNullable(lastTriggerDate);
    }

    public InstantFilter lastTriggerDate() {
        if (lastTriggerDate == null) {
            setLastTriggerDate(new InstantFilter());
        }
        return lastTriggerDate;
    }

    public void setLastTriggerDate(InstantFilter lastTriggerDate) {
        this.lastTriggerDate = lastTriggerDate;
    }

    public InstantFilter getLastSuccessDate() {
        return lastSuccessDate;
    }

    public Optional<InstantFilter> optionalLastSuccessDate() {
        return Optional.ofNullable(lastSuccessDate);
    }

    public InstantFilter lastSuccessDate() {
        if (lastSuccessDate == null) {
            setLastSuccessDate(new InstantFilter());
        }
        return lastSuccessDate;
    }

    public void setLastSuccessDate(InstantFilter lastSuccessDate) {
        this.lastSuccessDate = lastSuccessDate;
    }

    public IntegerFilter getFailureCount() {
        return failureCount;
    }

    public Optional<IntegerFilter> optionalFailureCount() {
        return Optional.ofNullable(failureCount);
    }

    public IntegerFilter failureCount() {
        if (failureCount == null) {
            setFailureCount(new IntegerFilter());
        }
        return failureCount;
    }

    public void setFailureCount(IntegerFilter failureCount) {
        this.failureCount = failureCount;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WebhookSubscriptionCriteria that = (WebhookSubscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(url, that.url) &&
            Objects.equals(secret, that.secret) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(retryCount, that.retryCount) &&
            Objects.equals(maxRetries, that.maxRetries) &&
            Objects.equals(retryDelay, that.retryDelay) &&
            Objects.equals(lastTriggerDate, that.lastTriggerDate) &&
            Objects.equals(lastSuccessDate, that.lastSuccessDate) &&
            Objects.equals(failureCount, that.failureCount) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            url,
            secret,
            isActive,
            retryCount,
            maxRetries,
            retryDelay,
            lastTriggerDate,
            lastSuccessDate,
            failureCount,
            createdBy,
            createdDate,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebhookSubscriptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalSecret().map(f -> "secret=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalRetryCount().map(f -> "retryCount=" + f + ", ").orElse("") +
            optionalMaxRetries().map(f -> "maxRetries=" + f + ", ").orElse("") +
            optionalRetryDelay().map(f -> "retryDelay=" + f + ", ").orElse("") +
            optionalLastTriggerDate().map(f -> "lastTriggerDate=" + f + ", ").orElse("") +
            optionalLastSuccessDate().map(f -> "lastSuccessDate=" + f + ", ").orElse("") +
            optionalFailureCount().map(f -> "failureCount=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
