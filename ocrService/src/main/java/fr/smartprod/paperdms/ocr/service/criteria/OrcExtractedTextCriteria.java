package fr.smartprod.paperdms.ocr.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ocr.domain.OrcExtractedText} entity. This class is used
 * in {@link fr.smartprod.paperdms.ocr.web.rest.OrcExtractedTextResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orc-extracted-texts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrcExtractedTextCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contentSha256;

    private StringFilter s3ContentKey;

    private StringFilter s3Bucket;

    private IntegerFilter pageNumber;

    private StringFilter language;

    private IntegerFilter wordCount;

    private BooleanFilter hasStructuredData;

    private StringFilter structuredDataS3Key;

    private InstantFilter extractedDate;

    private LongFilter jobId;

    private Boolean distinct;

    public OrcExtractedTextCriteria() {}

    public OrcExtractedTextCriteria(OrcExtractedTextCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.contentSha256 = other.optionalContentSha256().map(StringFilter::copy).orElse(null);
        this.s3ContentKey = other.optionals3ContentKey().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.wordCount = other.optionalWordCount().map(IntegerFilter::copy).orElse(null);
        this.hasStructuredData = other.optionalHasStructuredData().map(BooleanFilter::copy).orElse(null);
        this.structuredDataS3Key = other.optionalStructuredDataS3Key().map(StringFilter::copy).orElse(null);
        this.extractedDate = other.optionalExtractedDate().map(InstantFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrcExtractedTextCriteria copy() {
        return new OrcExtractedTextCriteria(this);
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

    public StringFilter getContentSha256() {
        return contentSha256;
    }

    public Optional<StringFilter> optionalContentSha256() {
        return Optional.ofNullable(contentSha256);
    }

    public StringFilter contentSha256() {
        if (contentSha256 == null) {
            setContentSha256(new StringFilter());
        }
        return contentSha256;
    }

    public void setContentSha256(StringFilter contentSha256) {
        this.contentSha256 = contentSha256;
    }

    public StringFilter gets3ContentKey() {
        return s3ContentKey;
    }

    public Optional<StringFilter> optionals3ContentKey() {
        return Optional.ofNullable(s3ContentKey);
    }

    public StringFilter s3ContentKey() {
        if (s3ContentKey == null) {
            sets3ContentKey(new StringFilter());
        }
        return s3ContentKey;
    }

    public void sets3ContentKey(StringFilter s3ContentKey) {
        this.s3ContentKey = s3ContentKey;
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

    public BooleanFilter getHasStructuredData() {
        return hasStructuredData;
    }

    public Optional<BooleanFilter> optionalHasStructuredData() {
        return Optional.ofNullable(hasStructuredData);
    }

    public BooleanFilter hasStructuredData() {
        if (hasStructuredData == null) {
            setHasStructuredData(new BooleanFilter());
        }
        return hasStructuredData;
    }

    public void setHasStructuredData(BooleanFilter hasStructuredData) {
        this.hasStructuredData = hasStructuredData;
    }

    public StringFilter getStructuredDataS3Key() {
        return structuredDataS3Key;
    }

    public Optional<StringFilter> optionalStructuredDataS3Key() {
        return Optional.ofNullable(structuredDataS3Key);
    }

    public StringFilter structuredDataS3Key() {
        if (structuredDataS3Key == null) {
            setStructuredDataS3Key(new StringFilter());
        }
        return structuredDataS3Key;
    }

    public void setStructuredDataS3Key(StringFilter structuredDataS3Key) {
        this.structuredDataS3Key = structuredDataS3Key;
    }

    public InstantFilter getExtractedDate() {
        return extractedDate;
    }

    public Optional<InstantFilter> optionalExtractedDate() {
        return Optional.ofNullable(extractedDate);
    }

    public InstantFilter extractedDate() {
        if (extractedDate == null) {
            setExtractedDate(new InstantFilter());
        }
        return extractedDate;
    }

    public void setExtractedDate(InstantFilter extractedDate) {
        this.extractedDate = extractedDate;
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
        final OrcExtractedTextCriteria that = (OrcExtractedTextCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contentSha256, that.contentSha256) &&
            Objects.equals(s3ContentKey, that.s3ContentKey) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(language, that.language) &&
            Objects.equals(wordCount, that.wordCount) &&
            Objects.equals(hasStructuredData, that.hasStructuredData) &&
            Objects.equals(structuredDataS3Key, that.structuredDataS3Key) &&
            Objects.equals(extractedDate, that.extractedDate) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            contentSha256,
            s3ContentKey,
            s3Bucket,
            pageNumber,
            language,
            wordCount,
            hasStructuredData,
            structuredDataS3Key,
            extractedDate,
            jobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrcExtractedTextCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalContentSha256().map(f -> "contentSha256=" + f + ", ").orElse("") +
            optionals3ContentKey().map(f -> "s3ContentKey=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalWordCount().map(f -> "wordCount=" + f + ", ").orElse("") +
            optionalHasStructuredData().map(f -> "hasStructuredData=" + f + ", ").orElse("") +
            optionalStructuredDataS3Key().map(f -> "structuredDataS3Key=" + f + ", ").orElse("") +
            optionalExtractedDate().map(f -> "extractedDate=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
