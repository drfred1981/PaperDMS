package fr.smartprod.paperdms.transform.service.criteria;

import fr.smartprod.paperdms.transform.domain.enumeration.RedactionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.transform.domain.TransformRedactionJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.transform.web.rest.TransformRedactionJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transform-redaction-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransformRedactionJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RedactionType
     */
    public static class RedactionTypeFilter extends Filter<RedactionType> {

        public RedactionTypeFilter() {}

        public RedactionTypeFilter(RedactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public RedactionTypeFilter copy() {
            return new RedactionTypeFilter(this);
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

    private StringFilter documentSha256;

    private RedactionTypeFilter redactionType;

    private StringFilter redactionColor;

    private StringFilter replaceWith;

    private StringFilter outputS3Key;

    private StringFilter outputDocumentSha256;

    private TransformStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public TransformRedactionJobCriteria() {}

    public TransformRedactionJobCriteria(TransformRedactionJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.redactionType = other.optionalRedactionType().map(RedactionTypeFilter::copy).orElse(null);
        this.redactionColor = other.optionalRedactionColor().map(StringFilter::copy).orElse(null);
        this.replaceWith = other.optionalReplaceWith().map(StringFilter::copy).orElse(null);
        this.outputS3Key = other.optionalOutputS3Key().map(StringFilter::copy).orElse(null);
        this.outputDocumentSha256 = other.optionalOutputDocumentSha256().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransformStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransformRedactionJobCriteria copy() {
        return new TransformRedactionJobCriteria(this);
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

    public RedactionTypeFilter getRedactionType() {
        return redactionType;
    }

    public Optional<RedactionTypeFilter> optionalRedactionType() {
        return Optional.ofNullable(redactionType);
    }

    public RedactionTypeFilter redactionType() {
        if (redactionType == null) {
            setRedactionType(new RedactionTypeFilter());
        }
        return redactionType;
    }

    public void setRedactionType(RedactionTypeFilter redactionType) {
        this.redactionType = redactionType;
    }

    public StringFilter getRedactionColor() {
        return redactionColor;
    }

    public Optional<StringFilter> optionalRedactionColor() {
        return Optional.ofNullable(redactionColor);
    }

    public StringFilter redactionColor() {
        if (redactionColor == null) {
            setRedactionColor(new StringFilter());
        }
        return redactionColor;
    }

    public void setRedactionColor(StringFilter redactionColor) {
        this.redactionColor = redactionColor;
    }

    public StringFilter getReplaceWith() {
        return replaceWith;
    }

    public Optional<StringFilter> optionalReplaceWith() {
        return Optional.ofNullable(replaceWith);
    }

    public StringFilter replaceWith() {
        if (replaceWith == null) {
            setReplaceWith(new StringFilter());
        }
        return replaceWith;
    }

    public void setReplaceWith(StringFilter replaceWith) {
        this.replaceWith = replaceWith;
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

    public StringFilter getOutputDocumentSha256() {
        return outputDocumentSha256;
    }

    public Optional<StringFilter> optionalOutputDocumentSha256() {
        return Optional.ofNullable(outputDocumentSha256);
    }

    public StringFilter outputDocumentSha256() {
        if (outputDocumentSha256 == null) {
            setOutputDocumentSha256(new StringFilter());
        }
        return outputDocumentSha256;
    }

    public void setOutputDocumentSha256(StringFilter outputDocumentSha256) {
        this.outputDocumentSha256 = outputDocumentSha256;
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
        final TransformRedactionJobCriteria that = (TransformRedactionJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(redactionType, that.redactionType) &&
            Objects.equals(redactionColor, that.redactionColor) &&
            Objects.equals(replaceWith, that.replaceWith) &&
            Objects.equals(outputS3Key, that.outputS3Key) &&
            Objects.equals(outputDocumentSha256, that.outputDocumentSha256) &&
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
            documentSha256,
            redactionType,
            redactionColor,
            replaceWith,
            outputS3Key,
            outputDocumentSha256,
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
        return "TransformRedactionJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalRedactionType().map(f -> "redactionType=" + f + ", ").orElse("") +
            optionalRedactionColor().map(f -> "redactionColor=" + f + ", ").orElse("") +
            optionalReplaceWith().map(f -> "replaceWith=" + f + ", ").orElse("") +
            optionalOutputS3Key().map(f -> "outputS3Key=" + f + ", ").orElse("") +
            optionalOutputDocumentSha256().map(f -> "outputDocumentSha256=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
