package com.ged.ocr.service.criteria;

import com.ged.ocr.domain.enumeration.OcrStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.ocr.domain.OcrJob} entity. This class is used
 * in {@link com.ged.ocr.web.rest.OcrJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ocr-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OcrStatus
     */
    public static class OcrStatusFilter extends Filter<OcrStatus> {

        public OcrStatusFilter() {}

        public OcrStatusFilter(OcrStatusFilter filter) {
            super(filter);
        }

        @Override
        public OcrStatusFilter copy() {
            return new OcrStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OcrStatusFilter status;

    private LongFilter documentId;

    private StringFilter s3Key;

    private StringFilter s3Bucket;

    private StringFilter language;

    private StringFilter tikaEndpoint;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private IntegerFilter pageCount;

    private IntegerFilter progress;

    private IntegerFilter retryCount;

    private IntegerFilter priority;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private LongFilter tikaConfigId;

    private Boolean distinct;

    public OcrJobCriteria() {}

    public OcrJobCriteria(OcrJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OcrStatusFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.tikaEndpoint = other.optionalTikaEndpoint().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.progress = other.optionalProgress().map(IntegerFilter::copy).orElse(null);
        this.retryCount = other.optionalRetryCount().map(IntegerFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.tikaConfigId = other.optionalTikaConfigId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OcrJobCriteria copy() {
        return new OcrJobCriteria(this);
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

    public OcrStatusFilter getStatus() {
        return status;
    }

    public Optional<OcrStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OcrStatusFilter status() {
        if (status == null) {
            setStatus(new OcrStatusFilter());
        }
        return status;
    }

    public void setStatus(OcrStatusFilter status) {
        this.status = status;
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

    public StringFilter gets3Bucket() {
        return s3Bucket;
    }

    public Optional<StringFilter> optionals3Bucket() {
        return Optional.ofNullable(s3Bucket);
    }

    public StringFilter s3Bucket() {
        if (s3Bucket == null) {
            sets3Bucket(new StringFilter());
        }
        return s3Bucket;
    }

    public void sets3Bucket(StringFilter s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public Optional<StringFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public StringFilter language() {
        if (language == null) {
            setLanguage(new StringFilter());
        }
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public StringFilter getTikaEndpoint() {
        return tikaEndpoint;
    }

    public Optional<StringFilter> optionalTikaEndpoint() {
        return Optional.ofNullable(tikaEndpoint);
    }

    public StringFilter tikaEndpoint() {
        if (tikaEndpoint == null) {
            setTikaEndpoint(new StringFilter());
        }
        return tikaEndpoint;
    }

    public void setTikaEndpoint(StringFilter tikaEndpoint) {
        this.tikaEndpoint = tikaEndpoint;
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

    public IntegerFilter getPageCount() {
        return pageCount;
    }

    public Optional<IntegerFilter> optionalPageCount() {
        return Optional.ofNullable(pageCount);
    }

    public IntegerFilter pageCount() {
        if (pageCount == null) {
            setPageCount(new IntegerFilter());
        }
        return pageCount;
    }

    public void setPageCount(IntegerFilter pageCount) {
        this.pageCount = pageCount;
    }

    public IntegerFilter getProgress() {
        return progress;
    }

    public Optional<IntegerFilter> optionalProgress() {
        return Optional.ofNullable(progress);
    }

    public IntegerFilter progress() {
        if (progress == null) {
            setProgress(new IntegerFilter());
        }
        return progress;
    }

    public void setProgress(IntegerFilter progress) {
        this.progress = progress;
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

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
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

    public LongFilter getTikaConfigId() {
        return tikaConfigId;
    }

    public Optional<LongFilter> optionalTikaConfigId() {
        return Optional.ofNullable(tikaConfigId);
    }

    public LongFilter tikaConfigId() {
        if (tikaConfigId == null) {
            setTikaConfigId(new LongFilter());
        }
        return tikaConfigId;
    }

    public void setTikaConfigId(LongFilter tikaConfigId) {
        this.tikaConfigId = tikaConfigId;
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
        final OcrJobCriteria that = (OcrJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(language, that.language) &&
            Objects.equals(tikaEndpoint, that.tikaEndpoint) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(progress, that.progress) &&
            Objects.equals(retryCount, that.retryCount) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(tikaConfigId, that.tikaConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            status,
            documentId,
            s3Key,
            s3Bucket,
            language,
            tikaEndpoint,
            startDate,
            endDate,
            pageCount,
            progress,
            retryCount,
            priority,
            createdDate,
            createdBy,
            tikaConfigId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalTikaEndpoint().map(f -> "tikaEndpoint=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalProgress().map(f -> "progress=" + f + ", ").orElse("") +
            optionalRetryCount().map(f -> "retryCount=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalTikaConfigId().map(f -> "tikaConfigId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
