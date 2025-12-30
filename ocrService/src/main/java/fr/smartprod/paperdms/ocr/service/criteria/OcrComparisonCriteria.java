package fr.smartprod.paperdms.ocr.service.criteria;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ocr.domain.OcrComparison} entity. This class is used
 * in {@link fr.smartprod.paperdms.ocr.web.rest.OcrComparisonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ocr-comparisons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrComparisonCriteria implements Serializable, Criteria {

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

    private IntegerFilter pageNumber;

    private DoubleFilter tikaConfidence;

    private DoubleFilter aiConfidence;

    private DoubleFilter similarity;

    private StringFilter differencesS3Key;

    private OcrEngineFilter selectedEngine;

    private StringFilter selectedBy;

    private InstantFilter selectedDate;

    private InstantFilter comparisonDate;

    private Boolean distinct;

    public OcrComparisonCriteria() {}

    public OcrComparisonCriteria(OcrComparisonCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.tikaConfidence = other.optionalTikaConfidence().map(DoubleFilter::copy).orElse(null);
        this.aiConfidence = other.optionalAiConfidence().map(DoubleFilter::copy).orElse(null);
        this.similarity = other.optionalSimilarity().map(DoubleFilter::copy).orElse(null);
        this.differencesS3Key = other.optionalDifferencesS3Key().map(StringFilter::copy).orElse(null);
        this.selectedEngine = other.optionalSelectedEngine().map(OcrEngineFilter::copy).orElse(null);
        this.selectedBy = other.optionalSelectedBy().map(StringFilter::copy).orElse(null);
        this.selectedDate = other.optionalSelectedDate().map(InstantFilter::copy).orElse(null);
        this.comparisonDate = other.optionalComparisonDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OcrComparisonCriteria copy() {
        return new OcrComparisonCriteria(this);
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

    public DoubleFilter getTikaConfidence() {
        return tikaConfidence;
    }

    public Optional<DoubleFilter> optionalTikaConfidence() {
        return Optional.ofNullable(tikaConfidence);
    }

    public DoubleFilter tikaConfidence() {
        if (tikaConfidence == null) {
            setTikaConfidence(new DoubleFilter());
        }
        return tikaConfidence;
    }

    public void setTikaConfidence(DoubleFilter tikaConfidence) {
        this.tikaConfidence = tikaConfidence;
    }

    public DoubleFilter getAiConfidence() {
        return aiConfidence;
    }

    public Optional<DoubleFilter> optionalAiConfidence() {
        return Optional.ofNullable(aiConfidence);
    }

    public DoubleFilter aiConfidence() {
        if (aiConfidence == null) {
            setAiConfidence(new DoubleFilter());
        }
        return aiConfidence;
    }

    public void setAiConfidence(DoubleFilter aiConfidence) {
        this.aiConfidence = aiConfidence;
    }

    public DoubleFilter getSimilarity() {
        return similarity;
    }

    public Optional<DoubleFilter> optionalSimilarity() {
        return Optional.ofNullable(similarity);
    }

    public DoubleFilter similarity() {
        if (similarity == null) {
            setSimilarity(new DoubleFilter());
        }
        return similarity;
    }

    public void setSimilarity(DoubleFilter similarity) {
        this.similarity = similarity;
    }

    public StringFilter getDifferencesS3Key() {
        return differencesS3Key;
    }

    public Optional<StringFilter> optionalDifferencesS3Key() {
        return Optional.ofNullable(differencesS3Key);
    }

    public StringFilter differencesS3Key() {
        if (differencesS3Key == null) {
            setDifferencesS3Key(new StringFilter());
        }
        return differencesS3Key;
    }

    public void setDifferencesS3Key(StringFilter differencesS3Key) {
        this.differencesS3Key = differencesS3Key;
    }

    public OcrEngineFilter getSelectedEngine() {
        return selectedEngine;
    }

    public Optional<OcrEngineFilter> optionalSelectedEngine() {
        return Optional.ofNullable(selectedEngine);
    }

    public OcrEngineFilter selectedEngine() {
        if (selectedEngine == null) {
            setSelectedEngine(new OcrEngineFilter());
        }
        return selectedEngine;
    }

    public void setSelectedEngine(OcrEngineFilter selectedEngine) {
        this.selectedEngine = selectedEngine;
    }

    public StringFilter getSelectedBy() {
        return selectedBy;
    }

    public Optional<StringFilter> optionalSelectedBy() {
        return Optional.ofNullable(selectedBy);
    }

    public StringFilter selectedBy() {
        if (selectedBy == null) {
            setSelectedBy(new StringFilter());
        }
        return selectedBy;
    }

    public void setSelectedBy(StringFilter selectedBy) {
        this.selectedBy = selectedBy;
    }

    public InstantFilter getSelectedDate() {
        return selectedDate;
    }

    public Optional<InstantFilter> optionalSelectedDate() {
        return Optional.ofNullable(selectedDate);
    }

    public InstantFilter selectedDate() {
        if (selectedDate == null) {
            setSelectedDate(new InstantFilter());
        }
        return selectedDate;
    }

    public void setSelectedDate(InstantFilter selectedDate) {
        this.selectedDate = selectedDate;
    }

    public InstantFilter getComparisonDate() {
        return comparisonDate;
    }

    public Optional<InstantFilter> optionalComparisonDate() {
        return Optional.ofNullable(comparisonDate);
    }

    public InstantFilter comparisonDate() {
        if (comparisonDate == null) {
            setComparisonDate(new InstantFilter());
        }
        return comparisonDate;
    }

    public void setComparisonDate(InstantFilter comparisonDate) {
        this.comparisonDate = comparisonDate;
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
        final OcrComparisonCriteria that = (OcrComparisonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(tikaConfidence, that.tikaConfidence) &&
            Objects.equals(aiConfidence, that.aiConfidence) &&
            Objects.equals(similarity, that.similarity) &&
            Objects.equals(differencesS3Key, that.differencesS3Key) &&
            Objects.equals(selectedEngine, that.selectedEngine) &&
            Objects.equals(selectedBy, that.selectedBy) &&
            Objects.equals(selectedDate, that.selectedDate) &&
            Objects.equals(comparisonDate, that.comparisonDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            pageNumber,
            tikaConfidence,
            aiConfidence,
            similarity,
            differencesS3Key,
            selectedEngine,
            selectedBy,
            selectedDate,
            comparisonDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrComparisonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalTikaConfidence().map(f -> "tikaConfidence=" + f + ", ").orElse("") +
            optionalAiConfidence().map(f -> "aiConfidence=" + f + ", ").orElse("") +
            optionalSimilarity().map(f -> "similarity=" + f + ", ").orElse("") +
            optionalDifferencesS3Key().map(f -> "differencesS3Key=" + f + ", ").orElse("") +
            optionalSelectedEngine().map(f -> "selectedEngine=" + f + ", ").orElse("") +
            optionalSelectedBy().map(f -> "selectedBy=" + f + ", ").orElse("") +
            optionalSelectedDate().map(f -> "selectedDate=" + f + ", ").orElse("") +
            optionalComparisonDate().map(f -> "comparisonDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
