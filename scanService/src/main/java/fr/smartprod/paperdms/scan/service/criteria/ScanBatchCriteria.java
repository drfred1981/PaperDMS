package fr.smartprod.paperdms.scan.service.criteria;

import fr.smartprod.paperdms.scan.domain.enumeration.BatchStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.scan.domain.ScanBatch} entity. This class is used
 * in {@link fr.smartprod.paperdms.scan.web.rest.ScanBatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scan-batches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScanBatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BatchStatus
     */
    public static class BatchStatusFilter extends Filter<BatchStatus> {

        public BatchStatusFilter() {}

        public BatchStatusFilter(BatchStatusFilter filter) {
            super(filter);
        }

        @Override
        public BatchStatusFilter copy() {
            return new BatchStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter totalJobs;

    private IntegerFilter completedJobs;

    private IntegerFilter totalPages;

    private BatchStatusFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ScanBatchCriteria() {}

    public ScanBatchCriteria(ScanBatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.totalJobs = other.optionalTotalJobs().map(IntegerFilter::copy).orElse(null);
        this.completedJobs = other.optionalCompletedJobs().map(IntegerFilter::copy).orElse(null);
        this.totalPages = other.optionalTotalPages().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(BatchStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ScanBatchCriteria copy() {
        return new ScanBatchCriteria(this);
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

    public IntegerFilter getTotalJobs() {
        return totalJobs;
    }

    public Optional<IntegerFilter> optionalTotalJobs() {
        return Optional.ofNullable(totalJobs);
    }

    public IntegerFilter totalJobs() {
        if (totalJobs == null) {
            setTotalJobs(new IntegerFilter());
        }
        return totalJobs;
    }

    public void setTotalJobs(IntegerFilter totalJobs) {
        this.totalJobs = totalJobs;
    }

    public IntegerFilter getCompletedJobs() {
        return completedJobs;
    }

    public Optional<IntegerFilter> optionalCompletedJobs() {
        return Optional.ofNullable(completedJobs);
    }

    public IntegerFilter completedJobs() {
        if (completedJobs == null) {
            setCompletedJobs(new IntegerFilter());
        }
        return completedJobs;
    }

    public void setCompletedJobs(IntegerFilter completedJobs) {
        this.completedJobs = completedJobs;
    }

    public IntegerFilter getTotalPages() {
        return totalPages;
    }

    public Optional<IntegerFilter> optionalTotalPages() {
        return Optional.ofNullable(totalPages);
    }

    public IntegerFilter totalPages() {
        if (totalPages == null) {
            setTotalPages(new IntegerFilter());
        }
        return totalPages;
    }

    public void setTotalPages(IntegerFilter totalPages) {
        this.totalPages = totalPages;
    }

    public BatchStatusFilter getStatus() {
        return status;
    }

    public Optional<BatchStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public BatchStatusFilter status() {
        if (status == null) {
            setStatus(new BatchStatusFilter());
        }
        return status;
    }

    public void setStatus(BatchStatusFilter status) {
        this.status = status;
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
        final ScanBatchCriteria that = (ScanBatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(totalJobs, that.totalJobs) &&
            Objects.equals(completedJobs, that.completedJobs) &&
            Objects.equals(totalPages, that.totalPages) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalJobs, completedJobs, totalPages, status, createdBy, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScanBatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTotalJobs().map(f -> "totalJobs=" + f + ", ").orElse("") +
            optionalCompletedJobs().map(f -> "completedJobs=" + f + ", ").orElse("") +
            optionalTotalPages().map(f -> "totalPages=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
