package fr.smartprod.paperdms.ai.service.criteria;

import fr.smartprod.paperdms.ai.domain.enumeration.AILanguageDetectionMethod;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ai.domain.AILanguageDetection} entity. This class is used
 * in {@link fr.smartprod.paperdms.ai.web.rest.AILanguageDetectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ai-language-detections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AILanguageDetectionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AILanguageDetectionMethod
     */
    public static class AILanguageDetectionMethodFilter extends Filter<AILanguageDetectionMethod> {

        public AILanguageDetectionMethodFilter() {}

        public AILanguageDetectionMethodFilter(AILanguageDetectionMethodFilter filter) {
            super(filter);
        }

        @Override
        public AILanguageDetectionMethodFilter copy() {
            return new AILanguageDetectionMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private StringFilter detectedLanguage;

    private DoubleFilter confidence;

    private AILanguageDetectionMethodFilter detectionMethod;

    private StringFilter resultCacheKey;

    private BooleanFilter isCached;

    private InstantFilter detectedDate;

    private StringFilter modelVersion;

    private LongFilter jobId;

    private Boolean distinct;

    public AILanguageDetectionCriteria() {}

    public AILanguageDetectionCriteria(AILanguageDetectionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.detectedLanguage = other.optionalDetectedLanguage().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.detectionMethod = other.optionalDetectionMethod().map(AILanguageDetectionMethodFilter::copy).orElse(null);
        this.resultCacheKey = other.optionalResultCacheKey().map(StringFilter::copy).orElse(null);
        this.isCached = other.optionalIsCached().map(BooleanFilter::copy).orElse(null);
        this.detectedDate = other.optionalDetectedDate().map(InstantFilter::copy).orElse(null);
        this.modelVersion = other.optionalModelVersion().map(StringFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AILanguageDetectionCriteria copy() {
        return new AILanguageDetectionCriteria(this);
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

    public StringFilter getDetectedLanguage() {
        return detectedLanguage;
    }

    public Optional<StringFilter> optionalDetectedLanguage() {
        return Optional.ofNullable(detectedLanguage);
    }

    public StringFilter detectedLanguage() {
        if (detectedLanguage == null) {
            setDetectedLanguage(new StringFilter());
        }
        return detectedLanguage;
    }

    public void setDetectedLanguage(StringFilter detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
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

    public AILanguageDetectionMethodFilter getDetectionMethod() {
        return detectionMethod;
    }

    public Optional<AILanguageDetectionMethodFilter> optionalDetectionMethod() {
        return Optional.ofNullable(detectionMethod);
    }

    public AILanguageDetectionMethodFilter detectionMethod() {
        if (detectionMethod == null) {
            setDetectionMethod(new AILanguageDetectionMethodFilter());
        }
        return detectionMethod;
    }

    public void setDetectionMethod(AILanguageDetectionMethodFilter detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public StringFilter getResultCacheKey() {
        return resultCacheKey;
    }

    public Optional<StringFilter> optionalResultCacheKey() {
        return Optional.ofNullable(resultCacheKey);
    }

    public StringFilter resultCacheKey() {
        if (resultCacheKey == null) {
            setResultCacheKey(new StringFilter());
        }
        return resultCacheKey;
    }

    public void setResultCacheKey(StringFilter resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public BooleanFilter getIsCached() {
        return isCached;
    }

    public Optional<BooleanFilter> optionalIsCached() {
        return Optional.ofNullable(isCached);
    }

    public BooleanFilter isCached() {
        if (isCached == null) {
            setIsCached(new BooleanFilter());
        }
        return isCached;
    }

    public void setIsCached(BooleanFilter isCached) {
        this.isCached = isCached;
    }

    public InstantFilter getDetectedDate() {
        return detectedDate;
    }

    public Optional<InstantFilter> optionalDetectedDate() {
        return Optional.ofNullable(detectedDate);
    }

    public InstantFilter detectedDate() {
        if (detectedDate == null) {
            setDetectedDate(new InstantFilter());
        }
        return detectedDate;
    }

    public void setDetectedDate(InstantFilter detectedDate) {
        this.detectedDate = detectedDate;
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
        final AILanguageDetectionCriteria that = (AILanguageDetectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(detectedLanguage, that.detectedLanguage) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(detectionMethod, that.detectionMethod) &&
            Objects.equals(resultCacheKey, that.resultCacheKey) &&
            Objects.equals(isCached, that.isCached) &&
            Objects.equals(detectedDate, that.detectedDate) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            detectedLanguage,
            confidence,
            detectionMethod,
            resultCacheKey,
            isCached,
            detectedDate,
            modelVersion,
            jobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AILanguageDetectionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalDetectedLanguage().map(f -> "detectedLanguage=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalDetectionMethod().map(f -> "detectionMethod=" + f + ", ").orElse("") +
            optionalResultCacheKey().map(f -> "resultCacheKey=" + f + ", ").orElse("") +
            optionalIsCached().map(f -> "isCached=" + f + ", ").orElse("") +
            optionalDetectedDate().map(f -> "detectedDate=" + f + ", ").orElse("") +
            optionalModelVersion().map(f -> "modelVersion=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
