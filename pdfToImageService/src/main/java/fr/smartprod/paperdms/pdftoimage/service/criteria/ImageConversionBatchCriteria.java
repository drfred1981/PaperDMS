package fr.smartprod.paperdms.pdftoimage.service.criteria;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImageConversionBatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-conversion-batches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionBatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ConversionStatus
     */
    public static class ConversionStatusFilter extends Filter<ConversionStatus> {

        public ConversionStatusFilter() {}

        public ConversionStatusFilter(ConversionStatusFilter filter) {
            super(filter);
        }

        @Override
        public ConversionStatusFilter copy() {
            return new ConversionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter batchName;

    private StringFilter description;

    private InstantFilter createdAt;

    private ConversionStatusFilter status;

    private IntegerFilter totalConversions;

    private IntegerFilter completedConversions;

    private IntegerFilter failedConversions;

    private InstantFilter startedAt;

    private InstantFilter completedAt;

    private LongFilter totalProcessingDuration;

    private LongFilter createdByUserId;

    private LongFilter conversionsId;

    private Boolean distinct;

    public ImageConversionBatchCriteria() {}

    public ImageConversionBatchCriteria(ImageConversionBatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.batchName = other.optionalBatchName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ConversionStatusFilter::copy).orElse(null);
        this.totalConversions = other.optionalTotalConversions().map(IntegerFilter::copy).orElse(null);
        this.completedConversions = other.optionalCompletedConversions().map(IntegerFilter::copy).orElse(null);
        this.failedConversions = other.optionalFailedConversions().map(IntegerFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.totalProcessingDuration = other.optionalTotalProcessingDuration().map(LongFilter::copy).orElse(null);
        this.createdByUserId = other.optionalCreatedByUserId().map(LongFilter::copy).orElse(null);
        this.conversionsId = other.optionalConversionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageConversionBatchCriteria copy() {
        return new ImageConversionBatchCriteria(this);
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

    public StringFilter getBatchName() {
        return batchName;
    }

    public Optional<StringFilter> optionalBatchName() {
        return Optional.ofNullable(batchName);
    }

    public StringFilter batchName() {
        if (batchName == null) {
            setBatchName(new StringFilter());
        }
        return batchName;
    }

    public void setBatchName(StringFilter batchName) {
        this.batchName = batchName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ConversionStatusFilter getStatus() {
        return status;
    }

    public Optional<ConversionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ConversionStatusFilter status() {
        if (status == null) {
            setStatus(new ConversionStatusFilter());
        }
        return status;
    }

    public void setStatus(ConversionStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getTotalConversions() {
        return totalConversions;
    }

    public Optional<IntegerFilter> optionalTotalConversions() {
        return Optional.ofNullable(totalConversions);
    }

    public IntegerFilter totalConversions() {
        if (totalConversions == null) {
            setTotalConversions(new IntegerFilter());
        }
        return totalConversions;
    }

    public void setTotalConversions(IntegerFilter totalConversions) {
        this.totalConversions = totalConversions;
    }

    public IntegerFilter getCompletedConversions() {
        return completedConversions;
    }

    public Optional<IntegerFilter> optionalCompletedConversions() {
        return Optional.ofNullable(completedConversions);
    }

    public IntegerFilter completedConversions() {
        if (completedConversions == null) {
            setCompletedConversions(new IntegerFilter());
        }
        return completedConversions;
    }

    public void setCompletedConversions(IntegerFilter completedConversions) {
        this.completedConversions = completedConversions;
    }

    public IntegerFilter getFailedConversions() {
        return failedConversions;
    }

    public Optional<IntegerFilter> optionalFailedConversions() {
        return Optional.ofNullable(failedConversions);
    }

    public IntegerFilter failedConversions() {
        if (failedConversions == null) {
            setFailedConversions(new IntegerFilter());
        }
        return failedConversions;
    }

    public void setFailedConversions(IntegerFilter failedConversions) {
        this.failedConversions = failedConversions;
    }

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public LongFilter getTotalProcessingDuration() {
        return totalProcessingDuration;
    }

    public Optional<LongFilter> optionalTotalProcessingDuration() {
        return Optional.ofNullable(totalProcessingDuration);
    }

    public LongFilter totalProcessingDuration() {
        if (totalProcessingDuration == null) {
            setTotalProcessingDuration(new LongFilter());
        }
        return totalProcessingDuration;
    }

    public void setTotalProcessingDuration(LongFilter totalProcessingDuration) {
        this.totalProcessingDuration = totalProcessingDuration;
    }

    public LongFilter getCreatedByUserId() {
        return createdByUserId;
    }

    public Optional<LongFilter> optionalCreatedByUserId() {
        return Optional.ofNullable(createdByUserId);
    }

    public LongFilter createdByUserId() {
        if (createdByUserId == null) {
            setCreatedByUserId(new LongFilter());
        }
        return createdByUserId;
    }

    public void setCreatedByUserId(LongFilter createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public LongFilter getConversionsId() {
        return conversionsId;
    }

    public Optional<LongFilter> optionalConversionsId() {
        return Optional.ofNullable(conversionsId);
    }

    public LongFilter conversionsId() {
        if (conversionsId == null) {
            setConversionsId(new LongFilter());
        }
        return conversionsId;
    }

    public void setConversionsId(LongFilter conversionsId) {
        this.conversionsId = conversionsId;
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
        final ImageConversionBatchCriteria that = (ImageConversionBatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(batchName, that.batchName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(totalConversions, that.totalConversions) &&
            Objects.equals(completedConversions, that.completedConversions) &&
            Objects.equals(failedConversions, that.failedConversions) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(totalProcessingDuration, that.totalProcessingDuration) &&
            Objects.equals(createdByUserId, that.createdByUserId) &&
            Objects.equals(conversionsId, that.conversionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            batchName,
            description,
            createdAt,
            status,
            totalConversions,
            completedConversions,
            failedConversions,
            startedAt,
            completedAt,
            totalProcessingDuration,
            createdByUserId,
            conversionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionBatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBatchName().map(f -> "batchName=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTotalConversions().map(f -> "totalConversions=" + f + ", ").orElse("") +
            optionalCompletedConversions().map(f -> "completedConversions=" + f + ", ").orElse("") +
            optionalFailedConversions().map(f -> "failedConversions=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalTotalProcessingDuration().map(f -> "totalProcessingDuration=" + f + ", ").orElse("") +
            optionalCreatedByUserId().map(f -> "createdByUserId=" + f + ", ").orElse("") +
            optionalConversionsId().map(f -> "conversionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
