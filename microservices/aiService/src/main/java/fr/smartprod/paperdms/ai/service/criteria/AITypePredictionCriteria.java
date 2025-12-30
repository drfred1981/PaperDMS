package fr.smartprod.paperdms.ai.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ai.domain.AITypePrediction} entity. This class is used
 * in {@link fr.smartprod.paperdms.ai.web.rest.AITypePredictionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ai-type-predictions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AITypePredictionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentTypeName;

    private DoubleFilter confidence;

    private StringFilter reason;

    private StringFilter modelVersion;

    private StringFilter predictionS3Key;

    private BooleanFilter isAccepted;

    private StringFilter acceptedBy;

    private InstantFilter acceptedDate;

    private InstantFilter predictionDate;

    private LongFilter jobId;

    private Boolean distinct;

    public AITypePredictionCriteria() {}

    public AITypePredictionCriteria(AITypePredictionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentTypeName = other.optionalDocumentTypeName().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.reason = other.optionalReason().map(StringFilter::copy).orElse(null);
        this.modelVersion = other.optionalModelVersion().map(StringFilter::copy).orElse(null);
        this.predictionS3Key = other.optionalPredictionS3Key().map(StringFilter::copy).orElse(null);
        this.isAccepted = other.optionalIsAccepted().map(BooleanFilter::copy).orElse(null);
        this.acceptedBy = other.optionalAcceptedBy().map(StringFilter::copy).orElse(null);
        this.acceptedDate = other.optionalAcceptedDate().map(InstantFilter::copy).orElse(null);
        this.predictionDate = other.optionalPredictionDate().map(InstantFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AITypePredictionCriteria copy() {
        return new AITypePredictionCriteria(this);
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

    public StringFilter getDocumentTypeName() {
        return documentTypeName;
    }

    public Optional<StringFilter> optionalDocumentTypeName() {
        return Optional.ofNullable(documentTypeName);
    }

    public StringFilter documentTypeName() {
        if (documentTypeName == null) {
            setDocumentTypeName(new StringFilter());
        }
        return documentTypeName;
    }

    public void setDocumentTypeName(StringFilter documentTypeName) {
        this.documentTypeName = documentTypeName;
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

    public StringFilter getReason() {
        return reason;
    }

    public Optional<StringFilter> optionalReason() {
        return Optional.ofNullable(reason);
    }

    public StringFilter reason() {
        if (reason == null) {
            setReason(new StringFilter());
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
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

    public StringFilter getPredictionS3Key() {
        return predictionS3Key;
    }

    public Optional<StringFilter> optionalPredictionS3Key() {
        return Optional.ofNullable(predictionS3Key);
    }

    public StringFilter predictionS3Key() {
        if (predictionS3Key == null) {
            setPredictionS3Key(new StringFilter());
        }
        return predictionS3Key;
    }

    public void setPredictionS3Key(StringFilter predictionS3Key) {
        this.predictionS3Key = predictionS3Key;
    }

    public BooleanFilter getIsAccepted() {
        return isAccepted;
    }

    public Optional<BooleanFilter> optionalIsAccepted() {
        return Optional.ofNullable(isAccepted);
    }

    public BooleanFilter isAccepted() {
        if (isAccepted == null) {
            setIsAccepted(new BooleanFilter());
        }
        return isAccepted;
    }

    public void setIsAccepted(BooleanFilter isAccepted) {
        this.isAccepted = isAccepted;
    }

    public StringFilter getAcceptedBy() {
        return acceptedBy;
    }

    public Optional<StringFilter> optionalAcceptedBy() {
        return Optional.ofNullable(acceptedBy);
    }

    public StringFilter acceptedBy() {
        if (acceptedBy == null) {
            setAcceptedBy(new StringFilter());
        }
        return acceptedBy;
    }

    public void setAcceptedBy(StringFilter acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public InstantFilter getAcceptedDate() {
        return acceptedDate;
    }

    public Optional<InstantFilter> optionalAcceptedDate() {
        return Optional.ofNullable(acceptedDate);
    }

    public InstantFilter acceptedDate() {
        if (acceptedDate == null) {
            setAcceptedDate(new InstantFilter());
        }
        return acceptedDate;
    }

    public void setAcceptedDate(InstantFilter acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public InstantFilter getPredictionDate() {
        return predictionDate;
    }

    public Optional<InstantFilter> optionalPredictionDate() {
        return Optional.ofNullable(predictionDate);
    }

    public InstantFilter predictionDate() {
        if (predictionDate == null) {
            setPredictionDate(new InstantFilter());
        }
        return predictionDate;
    }

    public void setPredictionDate(InstantFilter predictionDate) {
        this.predictionDate = predictionDate;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public Optional<LongFilter> optionalJobId() {
        return Optional.ofNullable(jobId);
    }

    public LongFilter jobId() {
        if (jobId == null) {
            setJobId(new LongFilter());
        }
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
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
        final AITypePredictionCriteria that = (AITypePredictionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentTypeName, that.documentTypeName) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(predictionS3Key, that.predictionS3Key) &&
            Objects.equals(isAccepted, that.isAccepted) &&
            Objects.equals(acceptedBy, that.acceptedBy) &&
            Objects.equals(acceptedDate, that.acceptedDate) &&
            Objects.equals(predictionDate, that.predictionDate) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentTypeName,
            confidence,
            reason,
            modelVersion,
            predictionS3Key,
            isAccepted,
            acceptedBy,
            acceptedDate,
            predictionDate,
            jobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AITypePredictionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentTypeName().map(f -> "documentTypeName=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalReason().map(f -> "reason=" + f + ", ").orElse("") +
            optionalModelVersion().map(f -> "modelVersion=" + f + ", ").orElse("") +
            optionalPredictionS3Key().map(f -> "predictionS3Key=" + f + ", ").orElse("") +
            optionalIsAccepted().map(f -> "isAccepted=" + f + ", ").orElse("") +
            optionalAcceptedBy().map(f -> "acceptedBy=" + f + ", ").orElse("") +
            optionalAcceptedDate().map(f -> "acceptedDate=" + f + ", ").orElse("") +
            optionalPredictionDate().map(f -> "predictionDate=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
