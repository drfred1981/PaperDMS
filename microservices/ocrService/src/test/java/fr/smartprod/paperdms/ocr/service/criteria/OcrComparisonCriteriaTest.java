package fr.smartprod.paperdms.ocr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OcrComparisonCriteriaTest {

    @Test
    void newOcrComparisonCriteriaHasAllFiltersNullTest() {
        var ocrComparisonCriteria = new OcrComparisonCriteria();
        assertThat(ocrComparisonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ocrComparisonCriteriaFluentMethodsCreatesFiltersTest() {
        var ocrComparisonCriteria = new OcrComparisonCriteria();

        setAllFilters(ocrComparisonCriteria);

        assertThat(ocrComparisonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ocrComparisonCriteriaCopyCreatesNullFilterTest() {
        var ocrComparisonCriteria = new OcrComparisonCriteria();
        var copy = ocrComparisonCriteria.copy();

        assertThat(ocrComparisonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrComparisonCriteria)
        );
    }

    @Test
    void ocrComparisonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ocrComparisonCriteria = new OcrComparisonCriteria();
        setAllFilters(ocrComparisonCriteria);

        var copy = ocrComparisonCriteria.copy();

        assertThat(ocrComparisonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrComparisonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ocrComparisonCriteria = new OcrComparisonCriteria();

        assertThat(ocrComparisonCriteria).hasToString("OcrComparisonCriteria{}");
    }

    private static void setAllFilters(OcrComparisonCriteria ocrComparisonCriteria) {
        ocrComparisonCriteria.id();
        ocrComparisonCriteria.documentSha256();
        ocrComparisonCriteria.pageNumber();
        ocrComparisonCriteria.tikaConfidence();
        ocrComparisonCriteria.aiConfidence();
        ocrComparisonCriteria.similarity();
        ocrComparisonCriteria.differencesS3Key();
        ocrComparisonCriteria.selectedEngine();
        ocrComparisonCriteria.selectedBy();
        ocrComparisonCriteria.selectedDate();
        ocrComparisonCriteria.comparisonDate();
        ocrComparisonCriteria.distinct();
    }

    private static Condition<OcrComparisonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getTikaConfidence()) &&
                condition.apply(criteria.getAiConfidence()) &&
                condition.apply(criteria.getSimilarity()) &&
                condition.apply(criteria.getDifferencesS3Key()) &&
                condition.apply(criteria.getSelectedEngine()) &&
                condition.apply(criteria.getSelectedBy()) &&
                condition.apply(criteria.getSelectedDate()) &&
                condition.apply(criteria.getComparisonDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OcrComparisonCriteria> copyFiltersAre(
        OcrComparisonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getTikaConfidence(), copy.getTikaConfidence()) &&
                condition.apply(criteria.getAiConfidence(), copy.getAiConfidence()) &&
                condition.apply(criteria.getSimilarity(), copy.getSimilarity()) &&
                condition.apply(criteria.getDifferencesS3Key(), copy.getDifferencesS3Key()) &&
                condition.apply(criteria.getSelectedEngine(), copy.getSelectedEngine()) &&
                condition.apply(criteria.getSelectedBy(), copy.getSelectedBy()) &&
                condition.apply(criteria.getSelectedDate(), copy.getSelectedDate()) &&
                condition.apply(criteria.getComparisonDate(), copy.getComparisonDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
