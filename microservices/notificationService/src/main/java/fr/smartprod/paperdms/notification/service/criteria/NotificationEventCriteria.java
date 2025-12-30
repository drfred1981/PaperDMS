package fr.smartprod.paperdms.notification.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.notification.domain.NotificationEvent} entity. This class is used
 * in {@link fr.smartprod.paperdms.notification.web.rest.NotificationEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventType;

    private StringFilter entityType;

    private StringFilter entityName;

    private StringFilter userId;

    private InstantFilter eventDate;

    private BooleanFilter processed;

    private InstantFilter processedDate;

    private Boolean distinct;

    public NotificationEventCriteria() {}

    public NotificationEventCriteria(NotificationEventCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(StringFilter::copy).orElse(null);
        this.entityType = other.optionalEntityType().map(StringFilter::copy).orElse(null);
        this.entityName = other.optionalEntityName().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.eventDate = other.optionalEventDate().map(InstantFilter::copy).orElse(null);
        this.processed = other.optionalProcessed().map(BooleanFilter::copy).orElse(null);
        this.processedDate = other.optionalProcessedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationEventCriteria copy() {
        return new NotificationEventCriteria(this);
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

    public StringFilter getEntityType() {
        return entityType;
    }

    public Optional<StringFilter> optionalEntityType() {
        return Optional.ofNullable(entityType);
    }

    public StringFilter entityType() {
        if (entityType == null) {
            setEntityType(new StringFilter());
        }
        return entityType;
    }

    public void setEntityType(StringFilter entityType) {
        this.entityType = entityType;
    }

    public StringFilter getEntityName() {
        return entityName;
    }

    public Optional<StringFilter> optionalEntityName() {
        return Optional.ofNullable(entityName);
    }

    public StringFilter entityName() {
        if (entityName == null) {
            setEntityName(new StringFilter());
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public InstantFilter getEventDate() {
        return eventDate;
    }

    public Optional<InstantFilter> optionalEventDate() {
        return Optional.ofNullable(eventDate);
    }

    public InstantFilter eventDate() {
        if (eventDate == null) {
            setEventDate(new InstantFilter());
        }
        return eventDate;
    }

    public void setEventDate(InstantFilter eventDate) {
        this.eventDate = eventDate;
    }

    public BooleanFilter getProcessed() {
        return processed;
    }

    public Optional<BooleanFilter> optionalProcessed() {
        return Optional.ofNullable(processed);
    }

    public BooleanFilter processed() {
        if (processed == null) {
            setProcessed(new BooleanFilter());
        }
        return processed;
    }

    public void setProcessed(BooleanFilter processed) {
        this.processed = processed;
    }

    public InstantFilter getProcessedDate() {
        return processedDate;
    }

    public Optional<InstantFilter> optionalProcessedDate() {
        return Optional.ofNullable(processedDate);
    }

    public InstantFilter processedDate() {
        if (processedDate == null) {
            setProcessedDate(new InstantFilter());
        }
        return processedDate;
    }

    public void setProcessedDate(InstantFilter processedDate) {
        this.processedDate = processedDate;
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
        final NotificationEventCriteria that = (NotificationEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(processed, that.processed) &&
            Objects.equals(processedDate, that.processedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, entityType, entityName, userId, eventDate, processed, processedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationEventCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalEntityType().map(f -> "entityType=" + f + ", ").orElse("") +
            optionalEntityName().map(f -> "entityName=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalEventDate().map(f -> "eventDate=" + f + ", ").orElse("") +
            optionalProcessed().map(f -> "processed=" + f + ", ").orElse("") +
            optionalProcessedDate().map(f -> "processedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
