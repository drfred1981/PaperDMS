package fr.smartprod.paperdms.ocr.service.criteria;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ocr.domain.OcrCache} entity. This class is used
 * in {@link fr.smartprod.paperdms.ocr.web.rest.OcrCacheResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ocr-caches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrCacheCriteria implements Serializable, Criteria {

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

    private StringFilter documentSha256;

    private OcrEngineFilter ocrEngine;

    private StringFilter language;

    private IntegerFilter pageCount;

    private DoubleFilter totalConfidence;

    private StringFilter s3ResultKey;

    private StringFilter s3Bucket;

    private StringFilter orcExtractedTextS3Key;

    private IntegerFilter hits;

    private InstantFilter lastAccessDate;

    private InstantFilter createdDate;

    private InstantFilter expirationDate;

    private Boolean distinct;

    public OcrCacheCriteria() {}

    public OcrCacheCriteria(OcrCacheCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.ocrEngine = other.optionalOcrEngine().map(OcrEngineFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.totalConfidence = other.optionalTotalConfidence().map(DoubleFilter::copy).orElse(null);
        this.s3ResultKey = other.optionals3ResultKey().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.orcExtractedTextS3Key = other.optionalOrcExtractedTextS3Key().map(StringFilter::copy).orElse(null);
        this.hits = other.optionalHits().map(IntegerFilter::copy).orElse(null);
        this.lastAccessDate = other.optionalLastAccessDate().map(InstantFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OcrCacheCriteria copy() {
        return new OcrCacheCriteria(this);
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

    public DoubleFilter getTotalConfidence() {
        return totalConfidence;
    }

    public Optional<DoubleFilter> optionalTotalConfidence() {
        return Optional.ofNullable(totalConfidence);
    }

    public DoubleFilter totalConfidence() {
        if (totalConfidence == null) {
            setTotalConfidence(new DoubleFilter());
        }
        return totalConfidence;
    }

    public void setTotalConfidence(DoubleFilter totalConfidence) {
        this.totalConfidence = totalConfidence;
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

    public StringFilter getOrcExtractedTextS3Key() {
        return orcExtractedTextS3Key;
    }

    public Optional<StringFilter> optionalOrcExtractedTextS3Key() {
        return Optional.ofNullable(orcExtractedTextS3Key);
    }

    public StringFilter orcExtractedTextS3Key() {
        if (orcExtractedTextS3Key == null) {
            setOrcExtractedTextS3Key(new StringFilter());
        }
        return orcExtractedTextS3Key;
    }

    public void setOrcExtractedTextS3Key(StringFilter orcExtractedTextS3Key) {
        this.orcExtractedTextS3Key = orcExtractedTextS3Key;
    }

    public IntegerFilter getHits() {
        return hits;
    }

    public Optional<IntegerFilter> optionalHits() {
        return Optional.ofNullable(hits);
    }

    public IntegerFilter hits() {
        if (hits == null) {
            setHits(new IntegerFilter());
        }
        return hits;
    }

    public void setHits(IntegerFilter hits) {
        this.hits = hits;
    }

    public InstantFilter getLastAccessDate() {
        return lastAccessDate;
    }

    public Optional<InstantFilter> optionalLastAccessDate() {
        return Optional.ofNullable(lastAccessDate);
    }

    public InstantFilter lastAccessDate() {
        if (lastAccessDate == null) {
            setLastAccessDate(new InstantFilter());
        }
        return lastAccessDate;
    }

    public void setLastAccessDate(InstantFilter lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
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

    public InstantFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<InstantFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public InstantFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new InstantFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(InstantFilter expirationDate) {
        this.expirationDate = expirationDate;
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
        final OcrCacheCriteria that = (OcrCacheCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(ocrEngine, that.ocrEngine) &&
            Objects.equals(language, that.language) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(totalConfidence, that.totalConfidence) &&
            Objects.equals(s3ResultKey, that.s3ResultKey) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(orcExtractedTextS3Key, that.orcExtractedTextS3Key) &&
            Objects.equals(hits, that.hits) &&
            Objects.equals(lastAccessDate, that.lastAccessDate) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            ocrEngine,
            language,
            pageCount,
            totalConfidence,
            s3ResultKey,
            s3Bucket,
            orcExtractedTextS3Key,
            hits,
            lastAccessDate,
            createdDate,
            expirationDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrCacheCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalOcrEngine().map(f -> "ocrEngine=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalTotalConfidence().map(f -> "totalConfidence=" + f + ", ").orElse("") +
            optionals3ResultKey().map(f -> "s3ResultKey=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionalOrcExtractedTextS3Key().map(f -> "orcExtractedTextS3Key=" + f + ", ").orElse("") +
            optionalHits().map(f -> "hits=" + f + ", ").orElse("") +
            optionalLastAccessDate().map(f -> "lastAccessDate=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
