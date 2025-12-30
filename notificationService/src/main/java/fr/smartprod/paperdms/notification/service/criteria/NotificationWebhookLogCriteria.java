package fr.smartprod.paperdms.notification.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookLog} entity. This class is used
 * in {@link fr.smartprod.paperdms.notification.web.rest.NotificationWebhookLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-webhook-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationWebhookLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventType;

    private IntegerFilter responseStatus;

    private LongFilter responseTime;

    private IntegerFilter attemptNumber;

    private BooleanFilter isSuccess;

    private InstantFilter sentDate;

    private LongFilter subscriptionId;

    private Boolean distinct;

    public NotificationWebhookLogCriteria() {}

    public NotificationWebhookLogCriteria(NotificationWebhookLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(StringFilter::copy).orElse(null);
        this.responseStatus = other.optionalResponseStatus().map(IntegerFilter::copy).orElse(null);
        this.responseTime = other.optionalResponseTime().map(LongFilter::copy).orElse(null);
        this.attemptNumber = other.optionalAttemptNumber().map(IntegerFilter::copy).orElse(null);
        this.isSuccess = other.optionalIsSuccess().map(BooleanFilter::copy).orElse(null);
        this.sentDate = other.optionalSentDate().map(InstantFilter::copy).orElse(null);
        this.subscriptionId = other.optionalSubscriptionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationWebhookLogCriteria copy() {
        return new NotificationWebhookLogCriteria(this);
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

    public StringFilter getEventType() {
        return eventType;
    }

    public Optional<StringFilter> optionalEventType() {
        return Optional.ofNullable(eventType);
    }

    public StringFilter eventType() {
        if (eventType == null) {
            setEventType(new StringFilter());
        }
        return eventType;
    }

    public void setEventType(StringFilter eventType) {
        this.eventType = eventType;
    }

    public IntegerFilter getResponseStatus() {
        return responseStatus;
    }

    public Optional<IntegerFilter> optionalResponseStatus() {
        return Optional.ofNullable(responseStatus);
    }

    public IntegerFilter responseStatus() {
        if (responseStatus == null) {
            setResponseStatus(new IntegerFilter());
        }
        return responseStatus;
    }

    public void setResponseStatus(IntegerFilter responseStatus) {
        this.responseStatus = responseStatus;
    }

    public LongFilter getResponseTime() {
        return responseTime;
    }

    public Optional<LongFilter> optionalResponseTime() {
        return Optional.ofNullable(responseTime);
    }

    public LongFilter responseTime() {
        if (responseTime == null) {
            setResponseTime(new LongFilter());
        }
        return responseTime;
    }

    public void setResponseTime(LongFilter responseTime) {
        this.responseTime = responseTime;
    }

    public IntegerFilter getAttemptNumber() {
        return attemptNumber;
    }

    public Optional<IntegerFilter> optionalAttemptNumber() {
        return Optional.ofNullable(attemptNumber);
    }

    public IntegerFilter attemptNumber() {
        if (attemptNumber == null) {
            setAttemptNumber(new IntegerFilter());
        }
        return attemptNumber;
    }

    public void setAttemptNumber(IntegerFilter attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public BooleanFilter getIsSuccess() {
        return isSuccess;
    }

    public Optional<BooleanFilter> optionalIsSuccess() {
        return Optional.ofNullable(isSuccess);
    }

    public BooleanFilter isSuccess() {
        if (isSuccess == null) {
            setIsSuccess(new BooleanFilter());
        }
        return isSuccess;
    }

    public void setIsSuccess(BooleanFilter isSuccess) {
        this.isSuccess = isSuccess;
    }

    public InstantFilter getSentDate() {
        return sentDate;
    }

    public Optional<InstantFilter> optionalSentDate() {
        return Optional.ofNullable(sentDate);
    }

    public InstantFilter sentDate() {
        if (sentDate == null) {
            setSentDate(new InstantFilter());
        }
        return sentDate;
    }

    public void setSentDate(InstantFilter sentDate) {
        this.sentDate = sentDate;
    }

    public LongFilter getSubscriptionId() {
        return subscriptionId;
    }

    public Optional<LongFilter> optionalSubscriptionId() {
        return Optional.ofNullable(subscriptionId);
    }

    public LongFilter subscriptionId() {
        if (subscriptionId == null) {
            setSubscriptionId(new LongFilter());
        }
        return subscriptionId;
    }

    public void setSubscriptionId(LongFilter subscriptionId) {
        this.subscriptionId = subscriptionId;
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
        final NotificationWebhookLogCriteria that = (NotificationWebhookLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(responseStatus, that.responseStatus) &&
            Objects.equals(responseTime, that.responseTime) &&
            Objects.equals(attemptNumber, that.attemptNumber) &&
            Objects.equals(isSuccess, that.isSuccess) &&
            Objects.equals(sentDate, that.sentDate) &&
            Objects.equals(subscriptionId, that.subscriptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, responseStatus, responseTime, attemptNumber, isSuccess, sentDate, subscriptionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationWebhookLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalResponseStatus().map(f -> "responseStatus=" + f + ", ").orElse("") +
            optionalResponseTime().map(f -> "responseTime=" + f + ", ").orElse("") +
            optionalAttemptNumber().map(f -> "attemptNumber=" + f + ", ").orElse("") +
            optionalIsSuccess().map(f -> "isSuccess=" + f + ", ").orElse("") +
            optionalSentDate().map(f -> "sentDate=" + f + ", ").orElse("") +
            optionalSubscriptionId().map(f -> "subscriptionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
