package fr.smartprod.paperdms.reporting.service.criteria;

import fr.smartprod.paperdms.reporting.domain.enumeration.ReportingExecutionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ReportingExecution} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ReportingExecutionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-executions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingExecutionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReportingExecutionStatus
     */
    public static class ReportingExecutionStatusFilter extends Filter<ReportingExecutionStatus> {

        public ReportingExecutionStatusFilter() {}

        public ReportingExecutionStatusFilter(ReportingExecutionStatusFilter filter) {
            super(filter);
        }

        @Override
        public ReportingExecutionStatusFilter copy() {
            return new ReportingExecutionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ReportingExecutionStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private IntegerFilter recordsProcessed;

    private StringFilter outputS3Key;

    private LongFilter outputSize;

    private LongFilter scheduledReportId;

    private Boolean distinct;

    public ReportingExecutionCriteria() {}

    public ReportingExecutionCriteria(ReportingExecutionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ReportingExecutionStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.recordsProcessed = other.optionalRecordsProcessed().map(IntegerFilter::copy).orElse(null);
        this.outputS3Key = other.optionalOutputS3Key().map(StringFilter::copy).orElse(null);
        this.outputSize = other.optionalOutputSize().map(LongFilter::copy).orElse(null);
        this.scheduledReportId = other.optionalScheduledReportId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportingExecutionCriteria copy() {
        return new ReportingExecutionCriteria(this);
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

    public ReportingExecutionStatusFilter getStatus() {
        return status;
    }

    public Optional<ReportingExecutionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ReportingExecutionStatusFilter status() {
        if (status == null) {
            setStatus(new ReportingExecutionStatusFilter());
        }
        return status;
    }

    public void setStatus(ReportingExecutionStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
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

    public StringFilter getOutputS3Key() {
        return outputS3Key;
    }

    public Optional<StringFilter> optionalOutputS3Key() {
        return Optional.ofNullable(outputS3Key);
    }

    public StringFilter outputS3Key() {
        if (outputS3Key == null) {
            setOutputS3Key(new StringFilter());
        }
        return outputS3Key;
    }

    public void setOutputS3Key(StringFilter outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public LongFilter getOutputSize() {
        return outputSize;
    }

    public Optional<LongFilter> optionalOutputSize() {
        return Optional.ofNullable(outputSize);
    }

    public LongFilter outputSize() {
        if (outputSize == null) {
            setOutputSize(new LongFilter());
        }
        return outputSize;
    }

    public void setOutputSize(LongFilter outputSize) {
        this.outputSize = outputSize;
    }

    public LongFilter getScheduledReportId() {
        return scheduledReportId;
    }

    public Optional<LongFilter> optionalScheduledReportId() {
        return Optional.ofNullable(scheduledReportId);
    }

    public LongFilter scheduledReportId() {
        if (scheduledReportId == null) {
            setScheduledReportId(new LongFilter());
        }
        return scheduledReportId;
    }

    public void setScheduledReportId(LongFilter scheduledReportId) {
        this.scheduledReportId = scheduledReportId;
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
        final ReportingExecutionCriteria that = (ReportingExecutionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(recordsProcessed, that.recordsProcessed) &&
            Objects.equals(outputS3Key, that.outputS3Key) &&
            Objects.equals(outputSize, that.outputSize) &&
            Objects.equals(scheduledReportId, that.scheduledReportId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, startDate, endDate, recordsProcessed, outputS3Key, outputSize, scheduledReportId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingExecutionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalRecordsProcessed().map(f -> "recordsProcessed=" + f + ", ").orElse("") +
            optionalOutputS3Key().map(f -> "outputS3Key=" + f + ", ").orElse("") +
            optionalOutputSize().map(f -> "outputSize=" + f + ", ").orElse("") +
            optionalScheduledReportId().map(f -> "scheduledReportId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
