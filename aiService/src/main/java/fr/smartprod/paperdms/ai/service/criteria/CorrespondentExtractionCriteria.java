package fr.smartprod.paperdms.ai.service.criteria;

import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ai.domain.CorrespondentExtraction} entity. This class is used
 * in {@link fr.smartprod.paperdms.ai.web.rest.CorrespondentExtractionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /correspondent-extractions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentExtractionCriteria implements Serializable, Criteria {

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

    private StringFilter documentSha256;

    private StringFilter extractedTextSha256;

    private StringFilter detectedLanguage;

    private DoubleFilter languageConfidence;

    private AiJobStatusFilter status;

    private StringFilter resultCacheKey;

    private BooleanFilter isCached;

    private StringFilter resultS3Key;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private IntegerFilter sendersCount;

    private IntegerFilter recipientsCount;

    private InstantFilter createdDate;

    private Boolean distinct;

    public CorrespondentExtractionCriteria() {}

    public CorrespondentExtractionCriteria(CorrespondentExtractionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.extractedTextSha256 = other.optionalExtractedTextSha256().map(StringFilter::copy).orElse(null);
        this.detectedLanguage = other.optionalDetectedLanguage().map(StringFilter::copy).orElse(null);
        this.languageConfidence = other.optionalLanguageConfidence().map(DoubleFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AiJobStatusFilter::copy).orElse(null);
        this.resultCacheKey = other.optionalResultCacheKey().map(StringFilter::copy).orElse(null);
        this.isCached = other.optionalIsCached().map(BooleanFilter::copy).orElse(null);
        this.resultS3Key = other.optionalResultS3Key().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.sendersCount = other.optionalSendersCount().map(IntegerFilter::copy).orElse(null);
        this.recipientsCount = other.optionalRecipientsCount().map(IntegerFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CorrespondentExtractionCriteria copy() {
        return new CorrespondentExtractionCriteria(this);
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

    public StringFilter getExtractedTextSha256() {
        return extractedTextSha256;
    }

    public Optional<StringFilter> optionalExtractedTextSha256() {
        return Optional.ofNullable(extractedTextSha256);
    }

    public StringFilter extractedTextSha256() {
        if (extractedTextSha256 == null) {
            setExtractedTextSha256(new StringFilter());
        }
        return extractedTextSha256;
    }

    public void setExtractedTextSha256(StringFilter extractedTextSha256) {
        this.extractedTextSha256 = extractedTextSha256;
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

    public StringFilter getResultS3Key() {
        return resultS3Key;
    }

    public Optional<StringFilter> optionalResultS3Key() {
        return Optional.ofNullable(resultS3Key);
    }

    public StringFilter resultS3Key() {
        if (resultS3Key == null) {
            setResultS3Key(new StringFilter());
        }
        return resultS3Key;
    }

    public void setResultS3Key(StringFilter resultS3Key) {
        this.resultS3Key = resultS3Key;
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

    public IntegerFilter getSendersCount() {
        return sendersCount;
    }

    public Optional<IntegerFilter> optionalSendersCount() {
        return Optional.ofNullable(sendersCount);
    }

    public IntegerFilter sendersCount() {
        if (sendersCount == null) {
            setSendersCount(new IntegerFilter());
        }
        return sendersCount;
    }

    public void setSendersCount(IntegerFilter sendersCount) {
        this.sendersCount = sendersCount;
    }

    public IntegerFilter getRecipientsCount() {
        return recipientsCount;
    }

    public Optional<IntegerFilter> optionalRecipientsCount() {
        return Optional.ofNullable(recipientsCount);
    }

    public IntegerFilter recipientsCount() {
        if (recipientsCount == null) {
            setRecipientsCount(new IntegerFilter());
        }
        return recipientsCount;
    }

    public void setRecipientsCount(IntegerFilter recipientsCount) {
        this.recipientsCount = recipientsCount;
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
        final CorrespondentExtractionCriteria that = (CorrespondentExtractionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(extractedTextSha256, that.extractedTextSha256) &&
            Objects.equals(detectedLanguage, that.detectedLanguage) &&
            Objects.equals(languageConfidence, that.languageConfidence) &&
            Objects.equals(status, that.status) &&
            Objects.equals(resultCacheKey, that.resultCacheKey) &&
            Objects.equals(isCached, that.isCached) &&
            Objects.equals(resultS3Key, that.resultS3Key) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(sendersCount, that.sendersCount) &&
            Objects.equals(recipientsCount, that.recipientsCount) &&
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
            extractedTextSha256,
            detectedLanguage,
            languageConfidence,
            status,
            resultCacheKey,
            isCached,
            resultS3Key,
            startDate,
            endDate,
            sendersCount,
            recipientsCount,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrespondentExtractionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalExtractedTextSha256().map(f -> "extractedTextSha256=" + f + ", ").orElse("") +
            optionalDetectedLanguage().map(f -> "detectedLanguage=" + f + ", ").orElse("") +
            optionalLanguageConfidence().map(f -> "languageConfidence=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalResultCacheKey().map(f -> "resultCacheKey=" + f + ", ").orElse("") +
            optionalIsCached().map(f -> "isCached=" + f + ", ").orElse("") +
            optionalResultS3Key().map(f -> "resultS3Key=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalSendersCount().map(f -> "sendersCount=" + f + ", ").orElse("") +
            optionalRecipientsCount().map(f -> "recipientsCount=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
