package fr.smartprod.paperdms.ocr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OcrJobCriteriaTest {

    @Test
    void newOcrJobCriteriaHasAllFiltersNullTest() {
        var ocrJobCriteria = new OcrJobCriteria();
        assertThat(ocrJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ocrJobCriteriaFluentMethodsCreatesFiltersTest() {
        var ocrJobCriteria = new OcrJobCriteria();

        setAllFilters(ocrJobCriteria);

        assertThat(ocrJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ocrJobCriteriaCopyCreatesNullFilterTest() {
        var ocrJobCriteria = new OcrJobCriteria();
        var copy = ocrJobCriteria.copy();

        assertThat(ocrJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrJobCriteria)
        );
    }

    @Test
    void ocrJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ocrJobCriteria = new OcrJobCriteria();
        setAllFilters(ocrJobCriteria);

        var copy = ocrJobCriteria.copy();

        assertThat(ocrJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ocrJobCriteria = new OcrJobCriteria();

        assertThat(ocrJobCriteria).hasToString("OcrJobCriteria{}");
    }

    private static void setAllFilters(OcrJobCriteria ocrJobCriteria) {
        ocrJobCriteria.id();
        ocrJobCriteria.status();
        ocrJobCriteria.documentSha256();
        ocrJobCriteria.s3Key();
        ocrJobCriteria.s3Bucket();
        ocrJobCriteria.requestedLanguage();
        ocrJobCriteria.detectedLanguage();
        ocrJobCriteria.languageConfidence();
        ocrJobCriteria.ocrEngine();
        ocrJobCriteria.tikaEndpoint();
        ocrJobCriteria.aiProvider();
        ocrJobCriteria.aiModel();
        ocrJobCriteria.resultCacheKey();
        ocrJobCriteria.isCached();
        ocrJobCriteria.startDate();
        ocrJobCriteria.endDate();
        ocrJobCriteria.pageCount();
        ocrJobCriteria.progress();
        ocrJobCriteria.retryCount();
        ocrJobCriteria.priority();
        ocrJobCriteria.processingTime();
        ocrJobCriteria.costEstimate();
        ocrJobCriteria.createdDate();
        ocrJobCriteria.createdBy();
        ocrJobCriteria.resultsId();
        ocrJobCriteria.extractedId();
        ocrJobCriteria.distinct();
    }

    private static Condition<OcrJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.gets3Bucket()) &&
                condition.apply(criteria.getRequestedLanguage()) &&
                condition.apply(criteria.getDetectedLanguage()) &&
                condition.apply(criteria.getLanguageConfidence()) &&
                condition.apply(criteria.getOcrEngine()) &&
                condition.apply(criteria.getTikaEndpoint()) &&
                condition.apply(criteria.getAiProvider()) &&
                condition.apply(criteria.getAiModel()) &&
                condition.apply(criteria.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getPageCount()) &&
                condition.apply(criteria.getProgress()) &&
                condition.apply(criteria.getRetryCount()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getProcessingTime()) &&
                condition.apply(criteria.getCostEstimate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getResultsId()) &&
                condition.apply(criteria.getExtractedId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OcrJobCriteria> copyFiltersAre(OcrJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.gets3Bucket(), copy.gets3Bucket()) &&
                condition.apply(criteria.getRequestedLanguage(), copy.getRequestedLanguage()) &&
                condition.apply(criteria.getDetectedLanguage(), copy.getDetectedLanguage()) &&
                condition.apply(criteria.getLanguageConfidence(), copy.getLanguageConfidence()) &&
                condition.apply(criteria.getOcrEngine(), copy.getOcrEngine()) &&
                condition.apply(criteria.getTikaEndpoint(), copy.getTikaEndpoint()) &&
                condition.apply(criteria.getAiProvider(), copy.getAiProvider()) &&
                condition.apply(criteria.getAiModel(), copy.getAiModel()) &&
                condition.apply(criteria.getResultCacheKey(), copy.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached(), copy.getIsCached()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getPageCount(), copy.getPageCount()) &&
                condition.apply(criteria.getProgress(), copy.getProgress()) &&
                condition.apply(criteria.getRetryCount(), copy.getRetryCount()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getProcessingTime(), copy.getProcessingTime()) &&
                condition.apply(criteria.getCostEstimate(), copy.getCostEstimate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getResultsId(), copy.getResultsId()) &&
                condition.apply(criteria.getExtractedId(), copy.getExtractedId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
