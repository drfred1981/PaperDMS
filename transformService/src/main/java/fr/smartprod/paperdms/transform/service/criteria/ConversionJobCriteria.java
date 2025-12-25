package fr.smartprod.paperdms.transform.service.criteria;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.transform.domain.ConversionJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.transform.web.rest.ConversionJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conversion-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversionJobCriteria implements Serializable, Criteria {

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

    private LongFilter documentId;

    private StringFilter documentSha256;

    private StringFilter sourceFormat;

    private StringFilter targetFormat;

    private StringFilter conversionEngine;

    private StringFilter outputS3Key;

    private LongFilter outputDocumentId;

    private TransformStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ConversionJobCriteria() {}

    public ConversionJobCriteria(ConversionJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.sourceFormat = other.optionalSourceFormat().map(StringFilter::copy).orElse(null);
        this.targetFormat = other.optionalTargetFormat().map(StringFilter::copy).orElse(null);
        this.conversionEngine = other.optionalConversionEngine().map(StringFilter::copy).orElse(null);
        this.outputS3Key = other.optionalOutputS3Key().map(StringFilter::copy).orElse(null);
        this.outputDocumentId = other.optionalOutputDocumentId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransformStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConversionJobCriteria copy() {
        return new ConversionJobCriteria(this);
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

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public StringFilter getDocumentSha256() {
        return documentSha256;
    }

    public Optional<StringFilter> optionalDocumentSha256() {
        return Optional.ofNullable(documentSha256);
    }

    public StringFilter documentSha256() {
        if (documentSha256 == null) {
            setDocumentSha256(new StringFilter());
        }
        return documentSha256;
    }

    public void setDocumentSha256(StringFilter documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public StringFilter getSourceFormat() {
        return sourceFormat;
    }

    public Optional<StringFilter> optionalSourceFormat() {
        return Optional.ofNullable(sourceFormat);
    }

    public StringFilter sourceFormat() {
        if (sourceFormat == null) {
            setSourceFormat(new StringFilter());
        }
        return sourceFormat;
    }

    public void setSourceFormat(StringFilter sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public StringFilter getTargetFormat() {
        return targetFormat;
    }

    public Optional<StringFilter> optionalTargetFormat() {
        return Optional.ofNullable(targetFormat);
    }

    public StringFilter targetFormat() {
        if (targetFormat == null) {
            setTargetFormat(new StringFilter());
        }
        return targetFormat;
    }

    public void setTargetFormat(StringFilter targetFormat) {
        this.targetFormat = targetFormat;
    }

    public StringFilter getConversionEngine() {
        return conversionEngine;
    }

    public Optional<StringFilter> optionalConversionEngine() {
        return Optional.ofNullable(conversionEngine);
    }

    public StringFilter conversionEngine() {
        if (conversionEngine == null) {
            setConversionEngine(new StringFilter());
        }
        return conversionEngine;
    }

    public void setConversionEngine(StringFilter conversionEngine) {
        this.conversionEngine = conversionEngine;
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

    public LongFilter getOutputDocumentId() {
        return outputDocumentId;
    }

    public Optional<LongFilter> optionalOutputDocumentId() {
        return Optional.ofNullable(outputDocumentId);
    }

    public LongFilter outputDocumentId() {
        if (outputDocumentId == null) {
            setOutputDocumentId(new LongFilter());
        }
        return outputDocumentId;
    }

    public void setOutputDocumentId(LongFilter outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
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
        final ConversionJobCriteria that = (ConversionJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(sourceFormat, that.sourceFormat) &&
            Objects.equals(targetFormat, that.targetFormat) &&
            Objects.equals(conversionEngine, that.conversionEngine) &&
            Objects.equals(outputS3Key, that.outputS3Key) &&
            Objects.equals(outputDocumentId, that.outputDocumentId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            documentSha256,
            sourceFormat,
            targetFormat,
            conversionEngine,
            outputS3Key,
            outputDocumentId,
            status,
            startDate,
            endDate,
            createdBy,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversionJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalSourceFormat().map(f -> "sourceFormat=" + f + ", ").orElse("") +
            optionalTargetFormat().map(f -> "targetFormat=" + f + ", ").orElse("") +
            optionalConversionEngine().map(f -> "conversionEngine=" + f + ", ").orElse("") +
            optionalOutputS3Key().map(f -> "outputS3Key=" + f + ", ").orElse("") +
            optionalOutputDocumentId().map(f -> "outputDocumentId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
