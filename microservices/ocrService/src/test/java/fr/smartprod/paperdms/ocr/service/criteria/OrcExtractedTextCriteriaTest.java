package fr.smartprod.paperdms.ocr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrcExtractedTextCriteriaTest {

    @Test
    void newOrcExtractedTextCriteriaHasAllFiltersNullTest() {
        var orcExtractedTextCriteria = new OrcExtractedTextCriteria();
        assertThat(orcExtractedTextCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orcExtractedTextCriteriaFluentMethodsCreatesFiltersTest() {
        var orcExtractedTextCriteria = new OrcExtractedTextCriteria();

        setAllFilters(orcExtractedTextCriteria);

        assertThat(orcExtractedTextCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orcExtractedTextCriteriaCopyCreatesNullFilterTest() {
        var orcExtractedTextCriteria = new OrcExtractedTextCriteria();
        var copy = orcExtractedTextCriteria.copy();

        assertThat(orcExtractedTextCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orcExtractedTextCriteria)
        );
    }

    @Test
    void orcExtractedTextCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orcExtractedTextCriteria = new OrcExtractedTextCriteria();
        setAllFilters(orcExtractedTextCriteria);

        var copy = orcExtractedTextCriteria.copy();

        assertThat(orcExtractedTextCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orcExtractedTextCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orcExtractedTextCriteria = new OrcExtractedTextCriteria();

        assertThat(orcExtractedTextCriteria).hasToString("OrcExtractedTextCriteria{}");
    }

    private static void setAllFilters(OrcExtractedTextCriteria orcExtractedTextCriteria) {
        orcExtractedTextCriteria.id();
        orcExtractedTextCriteria.contentSha256();
        orcExtractedTextCriteria.s3ContentKey();
        orcExtractedTextCriteria.s3Bucket();
        orcExtractedTextCriteria.pageNumber();
        orcExtractedTextCriteria.language();
        orcExtractedTextCriteria.wordCount();
        orcExtractedTextCriteria.hasStructuredData();
        orcExtractedTextCriteria.structuredDataS3Key();
        orcExtractedTextCriteria.extractedDate();
        orcExtractedTextCriteria.jobId();
        orcExtractedTextCriteria.distinct();
    }

    private static Condition<OrcExtractedTextCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getContentSha256()) &&
                condition.apply(criteria.gets3ContentKey()) &&
                condition.apply(criteria.gets3Bucket()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getWordCount()) &&
                condition.apply(criteria.getHasStructuredData()) &&
                condition.apply(criteria.getStructuredDataS3Key()) &&
                condition.apply(criteria.getExtractedDate()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrcExtractedTextCriteria> copyFiltersAre(
        OrcExtractedTextCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getContentSha256(), copy.getContentSha256()) &&
                condition.apply(criteria.gets3ContentKey(), copy.gets3ContentKey()) &&
                condition.apply(criteria.gets3Bucket(), copy.gets3Bucket()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getWordCount(), copy.getWordCount()) &&
                condition.apply(criteria.getHasStructuredData(), copy.getHasStructuredData()) &&
                condition.apply(criteria.getStructuredDataS3Key(), copy.getStructuredDataS3Key()) &&
                condition.apply(criteria.getExtractedDate(), copy.getExtractedDate()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
