package fr.smartprod.paperdms.ocr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OcrResultCriteriaTest {

    @Test
    void newOcrResultCriteriaHasAllFiltersNullTest() {
        var ocrResultCriteria = new OcrResultCriteria();
        assertThat(ocrResultCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ocrResultCriteriaFluentMethodsCreatesFiltersTest() {
        var ocrResultCriteria = new OcrResultCriteria();

        setAllFilters(ocrResultCriteria);

        assertThat(ocrResultCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ocrResultCriteriaCopyCreatesNullFilterTest() {
        var ocrResultCriteria = new OcrResultCriteria();
        var copy = ocrResultCriteria.copy();

        assertThat(ocrResultCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrResultCriteria)
        );
    }

    @Test
    void ocrResultCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ocrResultCriteria = new OcrResultCriteria();
        setAllFilters(ocrResultCriteria);

        var copy = ocrResultCriteria.copy();

        assertThat(ocrResultCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrResultCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ocrResultCriteria = new OcrResultCriteria();

        assertThat(ocrResultCriteria).hasToString("OcrResultCriteria{}");
    }

    private static void setAllFilters(OcrResultCriteria ocrResultCriteria) {
        ocrResultCriteria.id();
        ocrResultCriteria.pageNumber();
        ocrResultCriteria.pageSha256();
        ocrResultCriteria.confidence();
        ocrResultCriteria.s3ResultKey();
        ocrResultCriteria.s3Bucket();
        ocrResultCriteria.s3BoundingBoxKey();
        ocrResultCriteria.language();
        ocrResultCriteria.wordCount();
        ocrResultCriteria.ocrEngine();
        ocrResultCriteria.processingTime();
        ocrResultCriteria.rawResponseS3Key();
        ocrResultCriteria.processedDate();
        ocrResultCriteria.jobId();
        ocrResultCriteria.distinct();
    }

    private static Condition<OcrResultCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getPageSha256()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.gets3ResultKey()) &&
                condition.apply(criteria.gets3Bucket()) &&
                condition.apply(criteria.gets3BoundingBoxKey()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getWordCount()) &&
                condition.apply(criteria.getOcrEngine()) &&
                condition.apply(criteria.getProcessingTime()) &&
                condition.apply(criteria.getRawResponseS3Key()) &&
                condition.apply(criteria.getProcessedDate()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OcrResultCriteria> copyFiltersAre(OcrResultCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getPageSha256(), copy.getPageSha256()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.gets3ResultKey(), copy.gets3ResultKey()) &&
                condition.apply(criteria.gets3Bucket(), copy.gets3Bucket()) &&
                condition.apply(criteria.gets3BoundingBoxKey(), copy.gets3BoundingBoxKey()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getWordCount(), copy.getWordCount()) &&
                condition.apply(criteria.getOcrEngine(), copy.getOcrEngine()) &&
                condition.apply(criteria.getProcessingTime(), copy.getProcessingTime()) &&
                condition.apply(criteria.getRawResponseS3Key(), copy.getRawResponseS3Key()) &&
                condition.apply(criteria.getProcessedDate(), copy.getProcessedDate()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
