package fr.smartprod.paperdms.ocr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OcrCacheCriteriaTest {

    @Test
    void newOcrCacheCriteriaHasAllFiltersNullTest() {
        var ocrCacheCriteria = new OcrCacheCriteria();
        assertThat(ocrCacheCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ocrCacheCriteriaFluentMethodsCreatesFiltersTest() {
        var ocrCacheCriteria = new OcrCacheCriteria();

        setAllFilters(ocrCacheCriteria);

        assertThat(ocrCacheCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ocrCacheCriteriaCopyCreatesNullFilterTest() {
        var ocrCacheCriteria = new OcrCacheCriteria();
        var copy = ocrCacheCriteria.copy();

        assertThat(ocrCacheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrCacheCriteria)
        );
    }

    @Test
    void ocrCacheCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ocrCacheCriteria = new OcrCacheCriteria();
        setAllFilters(ocrCacheCriteria);

        var copy = ocrCacheCriteria.copy();

        assertThat(ocrCacheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ocrCacheCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ocrCacheCriteria = new OcrCacheCriteria();

        assertThat(ocrCacheCriteria).hasToString("OcrCacheCriteria{}");
    }

    private static void setAllFilters(OcrCacheCriteria ocrCacheCriteria) {
        ocrCacheCriteria.id();
        ocrCacheCriteria.documentSha256();
        ocrCacheCriteria.ocrEngine();
        ocrCacheCriteria.language();
        ocrCacheCriteria.pageCount();
        ocrCacheCriteria.totalConfidence();
        ocrCacheCriteria.s3ResultKey();
        ocrCacheCriteria.s3Bucket();
        ocrCacheCriteria.orcExtractedTextS3Key();
        ocrCacheCriteria.hits();
        ocrCacheCriteria.lastAccessDate();
        ocrCacheCriteria.createdDate();
        ocrCacheCriteria.expirationDate();
        ocrCacheCriteria.distinct();
    }

    private static Condition<OcrCacheCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getOcrEngine()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getPageCount()) &&
                condition.apply(criteria.getTotalConfidence()) &&
                condition.apply(criteria.gets3ResultKey()) &&
                condition.apply(criteria.gets3Bucket()) &&
                condition.apply(criteria.getOrcExtractedTextS3Key()) &&
                condition.apply(criteria.getHits()) &&
                condition.apply(criteria.getLastAccessDate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getExpirationDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OcrCacheCriteria> copyFiltersAre(OcrCacheCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getOcrEngine(), copy.getOcrEngine()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getPageCount(), copy.getPageCount()) &&
                condition.apply(criteria.getTotalConfidence(), copy.getTotalConfidence()) &&
                condition.apply(criteria.gets3ResultKey(), copy.gets3ResultKey()) &&
                condition.apply(criteria.gets3Bucket(), copy.gets3Bucket()) &&
                condition.apply(criteria.getOrcExtractedTextS3Key(), copy.getOrcExtractedTextS3Key()) &&
                condition.apply(criteria.getHits(), copy.getHits()) &&
                condition.apply(criteria.getLastAccessDate(), copy.getLastAccessDate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getExpirationDate(), copy.getExpirationDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
