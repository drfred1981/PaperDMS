package fr.smartprod.paperdms.ocr.service.criteria;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ocr.domain.OcrResult} entity. This class is used
 * in {@link fr.smartprod.paperdms.ocr.web.rest.OcrResultResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ocr-results?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrResultCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter pageNumber;

    private StringFilter pageSha256;

    private DoubleFilter confidence;

    private StringFilter s3ResultKey;

    private StringFilter s3Bucket;

    private StringFilter s3BoundingBoxKey;

    private StringFilter language;

    private IntegerFilter wordCount;

    private OcrEngineFilter ocrEngine;

    private LongFilter processingTime;

    private StringFilter rawResponseS3Key;

    private InstantFilter processedDate;

    private LongFilter jobId;

    private Boolean distinct;

    public OcrResultCriteria() {}

    public OcrResultCriteria(OcrResultCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.pageSha256 = other.optionalPageSha256().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.s3ResultKey = other.optionals3ResultKey().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.s3BoundingBoxKey = other.optionals3BoundingBoxKey().map(StringFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.wordCount = other.optionalWordCount().map(IntegerFilter::copy).orElse(null);
        this.ocrEngine = other.optionalOcrEngine().map(OcrEngineFilter::copy).orElse(null);
        this.processingTime = other.optionalProcessingTime().map(LongFilter::copy).orElse(null);
        this.rawResponseS3Key = other.optionalRawResponseS3Key().map(StringFilter::copy).orElse(null);
        this.processedDate = other.optionalProcessedDate().map(InstantFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OcrResultCriteria copy() {
        return new OcrResultCriteria(this);
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

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public Optional<IntegerFilter> optionalPageNumber() {
        return Optional.ofNullable(pageNumber);
    }

    public IntegerFilter pageNumber() {
        if (pageNumber == null) {
            setPageNumber(new IntegerFilter());
        }
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
    }

    public StringFilter getPageSha256() {
        return pageSha256;
    }

    public Optional<StringFilter> optionalPageSha256() {
        return Optional.ofNullable(pageSha256);
    }

    public StringFilter pageSha256() {
        if (pageSha256 == null) {
            setPageSha256(new StringFilter());
        }
        return pageSha256;
    }

    public void setPageSha256(StringFilter pageSha256) {
        this.pageSha256 = pageSha256;
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

    public StringFilter gets3ResultKey() {
        return s3ResultKey;
    }

    public Optional<StringFilter> optionals3ResultKey() {
        return Optional.ofNullable(s3ResultKey);
    }

    public StringFilter s3ResultKey() {
        if (s3ResultKey == null) {
            sets3ResultKey(new StringFilter());
        }
        return s3ResultKey;
    }

    public void sets3ResultKey(StringFilter s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
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

    public StringFilter gets3BoundingBoxKey() {
        return s3BoundingBoxKey;
    }

    public Optional<StringFilter> optionals3BoundingBoxKey() {
        return Optional.ofNullable(s3BoundingBoxKey);
    }

    public StringFilter s3BoundingBoxKey() {
        if (s3BoundingBoxKey == null) {
            sets3BoundingBoxKey(new StringFilter());
        }
        return s3BoundingBoxKey;
    }

    public void sets3BoundingBoxKey(StringFilter s3BoundingBoxKey) {
        this.s3BoundingBoxKey = s3BoundingBoxKey;
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

    public IntegerFilter getWordCount() {
        return wordCount;
    }

    public Optional<IntegerFilter> optionalWordCount() {
        return Optional.ofNullable(wordCount);
    }

    public IntegerFilter wordCount() {
        if (wordCount == null) {
            setWordCount(new IntegerFilter());
        }
        return wordCount;
    }

    public void setWordCount(IntegerFilter wordCount) {
        this.wordCount = wordCount;
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

    public StringFilter getRawResponseS3Key() {
        return rawResponseS3Key;
    }

    public Optional<StringFilter> optionalRawResponseS3Key() {
        return Optional.ofNullable(rawResponseS3Key);
    }

    public StringFilter rawResponseS3Key() {
        if (rawResponseS3Key == null) {
            setRawResponseS3Key(new StringFilter());
        }
        return rawResponseS3Key;
    }

    public void setRawResponseS3Key(StringFilter rawResponseS3Key) {
        this.rawResponseS3Key = rawResponseS3Key;
    }

    public InstantFilter getProcessedDate() {
        return processedDate;
    }

    public Optional<InstantFilter> optionalProcessedDate() {
        return Optional.ofNullable(processedDate);
    }

    public InstantFilter processedDate() {
        if (processedDate == null) {
            setProcessedDate(new InstantFilter());
        }
        return processedDate;
    }

    public void setProcessedDate(InstantFilter processedDate) {
        this.processedDate = processedDate;
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
        final OcrResultCriteria that = (OcrResultCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(pageSha256, that.pageSha256) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(s3ResultKey, that.s3ResultKey) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(s3BoundingBoxKey, that.s3BoundingBoxKey) &&
            Objects.equals(language, that.language) &&
            Objects.equals(wordCount, that.wordCount) &&
            Objects.equals(ocrEngine, that.ocrEngine) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(rawResponseS3Key, that.rawResponseS3Key) &&
            Objects.equals(processedDate, that.processedDate) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pageNumber,
            pageSha256,
            confidence,
            s3ResultKey,
            s3Bucket,
            s3BoundingBoxKey,
            language,
            wordCount,
            ocrEngine,
            processingTime,
            rawResponseS3Key,
            processedDate,
            jobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrResultCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalPageSha256().map(f -> "pageSha256=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionals3ResultKey().map(f -> "s3ResultKey=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionals3BoundingBoxKey().map(f -> "s3BoundingBoxKey=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalWordCount().map(f -> "wordCount=" + f + ", ").orElse("") +
            optionalOcrEngine().map(f -> "ocrEngine=" + f + ", ").orElse("") +
            optionalProcessingTime().map(f -> "processingTime=" + f + ", ").orElse("") +
            optionalRawResponseS3Key().map(f -> "rawResponseS3Key=" + f + ", ").orElse("") +
            optionalProcessedDate().map(f -> "processedDate=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
