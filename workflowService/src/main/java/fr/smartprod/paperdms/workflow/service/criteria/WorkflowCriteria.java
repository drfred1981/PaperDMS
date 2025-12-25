package fr.smartprod.paperdms.workflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.workflow.domain.Workflow} entity. This class is used
 * in {@link fr.smartprod.paperdms.workflow.web.rest.WorkflowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workflows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter version;

    private BooleanFilter isActive;

    private BooleanFilter isParallel;

    private BooleanFilter autoStart;

    private StringFilter triggerEvent;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private Boolean distinct;

    public WorkflowCriteria() {}

    public WorkflowCriteria(WorkflowCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(IntegerFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.isParallel = other.optionalIsParallel().map(BooleanFilter::copy).orElse(null);
        this.autoStart = other.optionalAutoStart().map(BooleanFilter::copy).orElse(null);
        this.triggerEvent = other.optionalTriggerEvent().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkflowCriteria copy() {
        return new WorkflowCriteria(this);
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

    public IntegerFilter getVersion() {
        return version;
    }

    public Optional<IntegerFilter> optionalVersion() {
        return Optional.ofNullable(version);
    }

    public IntegerFilter version() {
        if (version == null) {
            setVersion(new IntegerFilter());
        }
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
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

    public BooleanFilter getIsParallel() {
        return isParallel;
    }

    public Optional<BooleanFilter> optionalIsParallel() {
        return Optional.ofNullable(isParallel);
    }

    public BooleanFilter isParallel() {
        if (isParallel == null) {
            setIsParallel(new BooleanFilter());
        }
        return isParallel;
    }

    public void setIsParallel(BooleanFilter isParallel) {
        this.isParallel = isParallel;
    }

    public BooleanFilter getAutoStart() {
        return autoStart;
    }

    public Optional<BooleanFilter> optionalAutoStart() {
        return Optional.ofNullable(autoStart);
    }

    public BooleanFilter autoStart() {
        if (autoStart == null) {
            setAutoStart(new BooleanFilter());
        }
        return autoStart;
    }

    public void setAutoStart(BooleanFilter autoStart) {
        this.autoStart = autoStart;
    }

    public StringFilter getTriggerEvent() {
        return triggerEvent;
    }

    public Optional<StringFilter> optionalTriggerEvent() {
        return Optional.ofNullable(triggerEvent);
    }

    public StringFilter triggerEvent() {
        if (triggerEvent == null) {
            setTriggerEvent(new StringFilter());
        }
        return triggerEvent;
    }

    public void setTriggerEvent(StringFilter triggerEvent) {
        this.triggerEvent = triggerEvent;
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
        final WorkflowCriteria that = (WorkflowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(version, that.version) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isParallel, that.isParallel) &&
            Objects.equals(autoStart, that.autoStart) &&
            Objects.equals(triggerEvent, that.triggerEvent) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            version,
            isActive,
            isParallel,
            autoStart,
            triggerEvent,
            createdDate,
            lastModifiedDate,
            createdBy,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalIsParallel().map(f -> "isParallel=" + f + ", ").orElse("") +
            optionalAutoStart().map(f -> "autoStart=" + f + ", ").orElse("") +
            optionalTriggerEvent().map(f -> "triggerEvent=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
