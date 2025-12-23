package fr.smartprod.paperdms.ocr.service.criteria;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ocr.domain.OcrJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.ocr.web.rest.OcrJobResource} to receive all the possible filtering options from
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

    /**
     * Class for filtering OcrEngine
     */
    public static class OcrEngineFilter extends Filter<OcrEngine> {

        public OcrEngineFilter() {}

        public OcrEngineFilter(OcrEngineFilter filter) {
            super(filter);
        }

        @Override
        public OcrEngineFilter copy() {
            return new OcrEngineFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OcrStatusFilter status;

    private LongFilter documentId;

    private StringFilter documentSha256;

    private StringFilter s3Key;

    private StringFilter s3Bucket;

    private StringFilter requestedLanguage;

    private StringFilter detectedLanguage;

    private DoubleFilter languageConfidence;

    private OcrEngineFilter ocrEngine;

    private StringFilter tikaEndpoint;

    private StringFilter aiProvider;

    private StringFilter aiModel;

    private StringFilter resultCacheKey;

    private BooleanFilter isCached;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private IntegerFilter pageCount;

    private IntegerFilter progress;

    private IntegerFilter retryCount;

    private IntegerFilter priority;

    private LongFilter processingTime;

    private DoubleFilter costEstimate;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private Boolean distinct;

    public OcrJobCriteria() {}

    public OcrJobCriteria(OcrJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OcrStatusFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.requestedLanguage = other.optionalRequestedLanguage().map(StringFilter::copy).orElse(null);
        this.detectedLanguage = other.optionalDetectedLanguage().map(StringFilter::copy).orElse(null);
        this.languageConfidence = other.optionalLanguageConfidence().map(DoubleFilter::copy).orElse(null);
        this.ocrEngine = other.optionalOcrEngine().map(OcrEngineFilter::copy).orElse(null);
        this.tikaEndpoint = other.optionalTikaEndpoint().map(StringFilter::copy).orElse(null);
        this.aiProvider = other.optionalAiProvider().map(StringFilter::copy).orElse(null);
        this.aiModel = other.optionalAiModel().map(StringFilter::copy).orElse(null);
        this.resultCacheKey = other.optionalResultCacheKey().map(StringFilter::copy).orElse(null);
        this.isCached = other.optionalIsCached().map(BooleanFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.progress = other.optionalProgress().map(IntegerFilter::copy).orElse(null);
        this.retryCount = other.optionalRetryCount().map(IntegerFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.processingTime = other.optionalProcessingTime().map(LongFilter::copy).orElse(null);
        this.costEstimate = other.optionalCostEstimate().map(DoubleFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
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

    public StringFilter getRequestedLanguage() {
        return requestedLanguage;
    }

    public Optional<StringFilter> optionalRequestedLanguage() {
        return Optional.ofNullable(requestedLanguage);
    }

    public StringFilter requestedLanguage() {
        if (requestedLanguage == null) {
            setRequestedLanguage(new StringFilter());
        }
        return requestedLanguage;
    }

    public void setRequestedLanguage(StringFilter requestedLanguage) {
        this.requestedLanguage = requestedLanguage;
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

    public DoubleFilter getLanguageConfidence() {
        return languageConfidence;
    }

    public Optional<DoubleFilter> optionalLanguageConfidence() {
        return Optional.ofNullable(languageConfidence);
    }

    public DoubleFilter languageConfidence() {
        if (languageConfidence == null) {
            setLanguageConfidence(new DoubleFilter());
        }
        return languageConfidence;
    }

    public void setLanguageConfidence(DoubleFilter languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public OcrEngineFilter getOcrEngine() {
        return ocrEngine;
    }

    public Optional<OcrEngineFilter> optionalOcrEngine() {
        return Optional.ofNullable(ocrEngine);
    }

    public OcrEngineFilter ocrEngine() {
        if (ocrEngine == null) {
            setOcrEngine(new OcrEngineFilter());
        }
        return ocrEngine;
    }

    public void setOcrEngine(OcrEngineFilter ocrEngine) {
        this.ocrEngine = ocrEngine;
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

    public StringFilter getAiProvider() {
        return aiProvider;
    }

    public Optional<StringFilter> optionalAiProvider() {
        return Optional.ofNullable(aiProvider);
    }

    public StringFilter aiProvider() {
        if (aiProvider == null) {
            setAiProvider(new StringFilter());
        }
        return aiProvider;
    }

    public void setAiProvider(StringFilter aiProvider) {
        this.aiProvider = aiProvider;
    }

    public StringFilter getAiModel() {
        return aiModel;
    }

    public Optional<StringFilter> optionalAiModel() {
        return Optional.ofNullable(aiModel);
    }

    public StringFilter aiModel() {
        if (aiModel == null) {
            setAiModel(new StringFilter());
        }
        return aiModel;
    }

    public void setAiModel(StringFilter aiModel) {
        this.aiModel = aiModel;
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

    public LongFilter getProcessingTime() {
        return processingTime;
    }

    public Optional<LongFilter> optionalProcessingTime() {
        return Optional.ofNullable(processingTime);
    }

    public LongFilter processingTime() {
        if (processingTime == null) {
            setProcessingTime(new LongFilter());
        }
        return processingTime;
    }

    public void setProcessingTime(LongFilter processingTime) {
        this.processingTime = processingTime;
    }

    public DoubleFilter getCostEstimate() {
        return costEstimate;
    }

    public Optional<DoubleFilter> optionalCostEstimate() {
        return Optional.ofNullable(costEstimate);
    }

    public DoubleFilter costEstimate() {
        if (costEstimate == null) {
            setCostEstimate(new DoubleFilter());
        }
        return costEstimate;
    }

    public void setCostEstimate(DoubleFilter costEstimate) {
        this.costEstimate = costEstimate;
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
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(requestedLanguage, that.requestedLanguage) &&
            Objects.equals(detectedLanguage, that.detectedLanguage) &&
            Objects.equals(languageConfidence, that.languageConfidence) &&
            Objects.equals(ocrEngine, that.ocrEngine) &&
            Objects.equals(tikaEndpoint, that.tikaEndpoint) &&
            Objects.equals(aiProvider, that.aiProvider) &&
            Objects.equals(aiModel, that.aiModel) &&
            Objects.equals(resultCacheKey, that.resultCacheKey) &&
            Objects.equals(isCached, that.isCached) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(progress, that.progress) &&
            Objects.equals(retryCount, that.retryCount) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(costEstimate, that.costEstimate) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            status,
            documentId,
            documentSha256,
            s3Key,
            s3Bucket,
            requestedLanguage,
            detectedLanguage,
            languageConfidence,
            ocrEngine,
            tikaEndpoint,
            aiProvider,
            aiModel,
            resultCacheKey,
            isCached,
            startDate,
            endDate,
            pageCount,
            progress,
            retryCount,
            priority,
            processingTime,
            costEstimate,
            createdDate,
            createdBy,
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
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionalRequestedLanguage().map(f -> "requestedLanguage=" + f + ", ").orElse("") +
            optionalDetectedLanguage().map(f -> "detectedLanguage=" + f + ", ").orElse("") +
            optionalLanguageConfidence().map(f -> "languageConfidence=" + f + ", ").orElse("") +
            optionalOcrEngine().map(f -> "ocrEngine=" + f + ", ").orElse("") +
            optionalTikaEndpoint().map(f -> "tikaEndpoint=" + f + ", ").orElse("") +
            optionalAiProvider().map(f -> "aiProvider=" + f + ", ").orElse("") +
            optionalAiModel().map(f -> "aiModel=" + f + ", ").orElse("") +
            optionalResultCacheKey().map(f -> "resultCacheKey=" + f + ", ").orElse("") +
            optionalIsCached().map(f -> "isCached=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalProgress().map(f -> "progress=" + f + ", ").orElse("") +
            optionalRetryCount().map(f -> "retryCount=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalProcessingTime().map(f -> "processingTime=" + f + ", ").orElse("") +
            optionalCostEstimate().map(f -> "costEstimate=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
