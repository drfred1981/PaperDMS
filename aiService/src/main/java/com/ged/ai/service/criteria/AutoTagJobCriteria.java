package com.ged.ai.service.criteria;

import com.ged.ai.domain.enumeration.AiJobStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.ai.domain.AutoTagJob} entity. This class is used
 * in {@link com.ged.ai.web.rest.AutoTagJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-tag-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AutoTagJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AiJobStatus
     */
    public static class AiJobStatusFilter extends Filter<AiJobStatus> {

        public AiJobStatusFilter() {}

        public AiJobStatusFilter(AiJobStatusFilter filter) {
            super(filter);
        }

        @Override
        public AiJobStatusFilter copy() {
            return new AiJobStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter s3Key;

    private AiJobStatusFilter status;

    private StringFilter modelVersion;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private DoubleFilter confidence;

    private InstantFilter createdDate;

    private Boolean distinct;

    public AutoTagJobCriteria() {}

    public AutoTagJobCriteria(AutoTagJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AiJobStatusFilter::copy).orElse(null);
        this.modelVersion = other.optionalModelVersion().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AutoTagJobCriteria copy() {
        return new AutoTagJobCriteria(this);
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

    public StringFilter gets3Key() {
        return s3Key;
    }

    public Optional<StringFilter> optionals3Key() {
        return Optional.ofNullable(s3Key);
    }

    public StringFilter s3Key() {
        if (s3Key == null) {
            sets3Key(new StringFilter());
        }
        return s3Key;
    }

    public void sets3Key(StringFilter s3Key) {
        this.s3Key = s3Key;
    }

    public AiJobStatusFilter getStatus() {
        return status;
    }

    public Optional<AiJobStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AiJobStatusFilter status() {
        if (status == null) {
            setStatus(new AiJobStatusFilter());
        }
        return status;
    }

    public void setStatus(AiJobStatusFilter status) {
        this.status = status;
    }

    public StringFilter getModelVersion() {
        return modelVersion;
    }

    public Optional<StringFilter> optionalModelVersion() {
        return Optional.ofNullable(modelVersion);
    }

    public StringFilter modelVersion() {
        if (modelVersion == null) {
            setModelVersion(new StringFilter());
        }
        return modelVersion;
    }

    public void setModelVersion(StringFilter modelVersion) {
        this.modelVersion = modelVersion;
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

    public DoubleFilter getConfidence() {
        return confidence;
    }

    public Optional<DoubleFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public DoubleFilter confidence() {
        if (confidence == null) {
            setConfidence(new DoubleFilter());
        }
        return confidence;
    }

    public void setConfidence(DoubleFilter confidence) {
        this.confidence = confidence;
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
        final AutoTagJobCriteria that = (AutoTagJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(status, that.status) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentId, s3Key, status, modelVersion, startDate, endDate, confidence, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoTagJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalModelVersion().map(f -> "modelVersion=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
