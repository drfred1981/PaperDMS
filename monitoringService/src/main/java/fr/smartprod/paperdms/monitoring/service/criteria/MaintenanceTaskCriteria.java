package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MaintenanceType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.TransformStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.MaintenanceTask} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.MaintenanceTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintenance-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceTaskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MaintenanceType
     */
    public static class MaintenanceTypeFilter extends Filter<MaintenanceType> {

        public MaintenanceTypeFilter() {}

        public MaintenanceTypeFilter(MaintenanceTypeFilter filter) {
            super(filter);
        }

        @Override
        public MaintenanceTypeFilter copy() {
            return new MaintenanceTypeFilter(this);
        }
    }

    /**
     * Class for filtering TransformStatus
     */
    public static class TransformStatusFilter extends Filter<TransformStatus> {

        public TransformStatusFilter() {}

        public TransformStatusFilter(TransformStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransformStatusFilter copy() {
            return new TransformStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private MaintenanceTypeFilter taskType;

    private StringFilter schedule;

    private TransformStatusFilter status;

    private BooleanFilter isActive;

    private InstantFilter lastRun;

    private InstantFilter nextRun;

    private LongFilter duration;

    private IntegerFilter recordsProcessed;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public MaintenanceTaskCriteria() {}

    public MaintenanceTaskCriteria(MaintenanceTaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.taskType = other.optionalTaskType().map(MaintenanceTypeFilter::copy).orElse(null);
        this.schedule = other.optionalSchedule().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransformStatusFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.lastRun = other.optionalLastRun().map(InstantFilter::copy).orElse(null);
        this.nextRun = other.optionalNextRun().map(InstantFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(LongFilter::copy).orElse(null);
        this.recordsProcessed = other.optionalRecordsProcessed().map(IntegerFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaintenanceTaskCriteria copy() {
        return new MaintenanceTaskCriteria(this);
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

    public MaintenanceTypeFilter getTaskType() {
        return taskType;
    }

    public Optional<MaintenanceTypeFilter> optionalTaskType() {
        return Optional.ofNullable(taskType);
    }

    public MaintenanceTypeFilter taskType() {
        if (taskType == null) {
            setTaskType(new MaintenanceTypeFilter());
        }
        return taskType;
    }

    public void setTaskType(MaintenanceTypeFilter taskType) {
        this.taskType = taskType;
    }

    public StringFilter getSchedule() {
        return schedule;
    }

    public Optional<StringFilter> optionalSchedule() {
        return Optional.ofNullable(schedule);
    }

    public StringFilter schedule() {
        if (schedule == null) {
            setSchedule(new StringFilter());
        }
        return schedule;
    }

    public void setSchedule(StringFilter schedule) {
        this.schedule = schedule;
    }

    public TransformStatusFilter getStatus() {
        return status;
    }

    public Optional<TransformStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TransformStatusFilter status() {
        if (status == null) {
            setStatus(new TransformStatusFilter());
        }
        return status;
    }

    public void setStatus(TransformStatusFilter status) {
        this.status = status;
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

    public InstantFilter getLastRun() {
        return lastRun;
    }

    public Optional<InstantFilter> optionalLastRun() {
        return Optional.ofNullable(lastRun);
    }

    public InstantFilter lastRun() {
        if (lastRun == null) {
            setLastRun(new InstantFilter());
        }
        return lastRun;
    }

    public void setLastRun(InstantFilter lastRun) {
        this.lastRun = lastRun;
    }

    public InstantFilter getNextRun() {
        return nextRun;
    }

    public Optional<InstantFilter> optionalNextRun() {
        return Optional.ofNullable(nextRun);
    }

    public InstantFilter nextRun() {
        if (nextRun == null) {
            setNextRun(new InstantFilter());
        }
        return nextRun;
    }

    public void setNextRun(InstantFilter nextRun) {
        this.nextRun = nextRun;
    }

    public LongFilter getDuration() {
        return duration;
    }

    public Optional<LongFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public LongFilter duration() {
        if (duration == null) {
            setDuration(new LongFilter());
        }
        return duration;
    }

    public void setDuration(LongFilter duration) {
        this.duration = duration;
    }

    public IntegerFilter getRecordsProcessed() {
        return recordsProcessed;
    }

    public Optional<IntegerFilter> optionalRecordsProcessed() {
        return Optional.ofNullable(recordsProcessed);
    }

    public IntegerFilter recordsProcessed() {
        if (recordsProcessed == null) {
            setRecordsProcessed(new IntegerFilter());
        }
        return recordsProcessed;
    }

    public void setRecordsProcessed(IntegerFilter recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
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
        final MaintenanceTaskCriteria that = (MaintenanceTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(taskType, that.taskType) &&
            Objects.equals(schedule, that.schedule) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(lastRun, that.lastRun) &&
            Objects.equals(nextRun, that.nextRun) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(recordsProcessed, that.recordsProcessed) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            taskType,
            schedule,
            status,
            isActive,
            lastRun,
            nextRun,
            duration,
            recordsProcessed,
            createdBy,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceTaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTaskType().map(f -> "taskType=" + f + ", ").orElse("") +
            optionalSchedule().map(f -> "schedule=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLastRun().map(f -> "lastRun=" + f + ", ").orElse("") +
            optionalNextRun().map(f -> "nextRun=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalRecordsProcessed().map(f -> "recordsProcessed=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
