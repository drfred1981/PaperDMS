package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AILanguageDetectionCriteriaTest {

    @Test
    void newAILanguageDetectionCriteriaHasAllFiltersNullTest() {
        var aILanguageDetectionCriteria = new AILanguageDetectionCriteria();
        assertThat(aILanguageDetectionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aILanguageDetectionCriteriaFluentMethodsCreatesFiltersTest() {
        var aILanguageDetectionCriteria = new AILanguageDetectionCriteria();

        setAllFilters(aILanguageDetectionCriteria);

        assertThat(aILanguageDetectionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aILanguageDetectionCriteriaCopyCreatesNullFilterTest() {
        var aILanguageDetectionCriteria = new AILanguageDetectionCriteria();
        var copy = aILanguageDetectionCriteria.copy();

        assertThat(aILanguageDetectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aILanguageDetectionCriteria)
        );
    }

    @Test
    void aILanguageDetectionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aILanguageDetectionCriteria = new AILanguageDetectionCriteria();
        setAllFilters(aILanguageDetectionCriteria);

        var copy = aILanguageDetectionCriteria.copy();

        assertThat(aILanguageDetectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aILanguageDetectionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aILanguageDetectionCriteria = new AILanguageDetectionCriteria();

        assertThat(aILanguageDetectionCriteria).hasToString("AILanguageDetectionCriteria{}");
    }

    private static void setAllFilters(AILanguageDetectionCriteria aILanguageDetectionCriteria) {
        aILanguageDetectionCriteria.id();
        aILanguageDetectionCriteria.documentSha256();
        aILanguageDetectionCriteria.detectedLanguage();
        aILanguageDetectionCriteria.confidence();
        aILanguageDetectionCriteria.detectionMethod();
        aILanguageDetectionCriteria.resultCacheKey();
        aILanguageDetectionCriteria.isCached();
        aILanguageDetectionCriteria.detectedDate();
        aILanguageDetectionCriteria.modelVersion();
        aILanguageDetectionCriteria.jobId();
        aILanguageDetectionCriteria.distinct();
    }

    private static Condition<AILanguageDetectionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getDetectedLanguage()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getDetectionMethod()) &&
                condition.apply(criteria.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached()) &&
                condition.apply(criteria.getDetectedDate()) &&
                condition.apply(criteria.getModelVersion()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AILanguageDetectionCriteria> copyFiltersAre(
        AILanguageDetectionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getDetectedLanguage(), copy.getDetectedLanguage()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getDetectionMethod(), copy.getDetectionMethod()) &&
                condition.apply(criteria.getResultCacheKey(), copy.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached(), copy.getIsCached()) &&
                condition.apply(criteria.getDetectedDate(), copy.getDetectedDate()) &&
                condition.apply(criteria.getModelVersion(), copy.getModelVersion()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
