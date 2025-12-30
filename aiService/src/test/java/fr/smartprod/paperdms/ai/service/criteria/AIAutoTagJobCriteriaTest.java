package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AIAutoTagJobCriteriaTest {

    @Test
    void newAIAutoTagJobCriteriaHasAllFiltersNullTest() {
        var aIAutoTagJobCriteria = new AIAutoTagJobCriteria();
        assertThat(aIAutoTagJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aIAutoTagJobCriteriaFluentMethodsCreatesFiltersTest() {
        var aIAutoTagJobCriteria = new AIAutoTagJobCriteria();

        setAllFilters(aIAutoTagJobCriteria);

        assertThat(aIAutoTagJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aIAutoTagJobCriteriaCopyCreatesNullFilterTest() {
        var aIAutoTagJobCriteria = new AIAutoTagJobCriteria();
        var copy = aIAutoTagJobCriteria.copy();

        assertThat(aIAutoTagJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aIAutoTagJobCriteria)
        );
    }

    @Test
    void aIAutoTagJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aIAutoTagJobCriteria = new AIAutoTagJobCriteria();
        setAllFilters(aIAutoTagJobCriteria);

        var copy = aIAutoTagJobCriteria.copy();

        assertThat(aIAutoTagJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aIAutoTagJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aIAutoTagJobCriteria = new AIAutoTagJobCriteria();

        assertThat(aIAutoTagJobCriteria).hasToString("AIAutoTagJobCriteria{}");
    }

    private static void setAllFilters(AIAutoTagJobCriteria aIAutoTagJobCriteria) {
        aIAutoTagJobCriteria.id();
        aIAutoTagJobCriteria.documentSha256();
        aIAutoTagJobCriteria.s3Key();
        aIAutoTagJobCriteria.extractedTextSha256();
        aIAutoTagJobCriteria.status();
        aIAutoTagJobCriteria.modelVersion();
        aIAutoTagJobCriteria.resultCacheKey();
        aIAutoTagJobCriteria.isCached();
        aIAutoTagJobCriteria.startDate();
        aIAutoTagJobCriteria.endDate();
        aIAutoTagJobCriteria.createdDate();
        aIAutoTagJobCriteria.aITypePredictionId();
        aIAutoTagJobCriteria.languagePredictionId();
        aIAutoTagJobCriteria.aITagPredictionsId();
        aIAutoTagJobCriteria.aICorrespondentPredictionsId();
        aIAutoTagJobCriteria.distinct();
    }

    private static Condition<AIAutoTagJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.getExtractedTextSha256()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getModelVersion()) &&
                condition.apply(criteria.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getAITypePredictionId()) &&
                condition.apply(criteria.getLanguagePredictionId()) &&
                condition.apply(criteria.getAITagPredictionsId()) &&
                condition.apply(criteria.getAICorrespondentPredictionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AIAutoTagJobCriteria> copyFiltersAre(
        AIAutoTagJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.getExtractedTextSha256(), copy.getExtractedTextSha256()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getModelVersion(), copy.getModelVersion()) &&
                condition.apply(criteria.getResultCacheKey(), copy.getResultCacheKey()) &&
                condition.apply(criteria.getIsCached(), copy.getIsCached()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getAITypePredictionId(), copy.getAITypePredictionId()) &&
                condition.apply(criteria.getLanguagePredictionId(), copy.getLanguagePredictionId()) &&
                condition.apply(criteria.getAITagPredictionsId(), copy.getAITagPredictionsId()) &&
                condition.apply(criteria.getAICorrespondentPredictionsId(), copy.getAICorrespondentPredictionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
