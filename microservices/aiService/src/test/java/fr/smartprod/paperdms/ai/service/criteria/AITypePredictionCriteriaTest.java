package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AITypePredictionCriteriaTest {

    @Test
    void newAITypePredictionCriteriaHasAllFiltersNullTest() {
        var aITypePredictionCriteria = new AITypePredictionCriteria();
        assertThat(aITypePredictionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aITypePredictionCriteriaFluentMethodsCreatesFiltersTest() {
        var aITypePredictionCriteria = new AITypePredictionCriteria();

        setAllFilters(aITypePredictionCriteria);

        assertThat(aITypePredictionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aITypePredictionCriteriaCopyCreatesNullFilterTest() {
        var aITypePredictionCriteria = new AITypePredictionCriteria();
        var copy = aITypePredictionCriteria.copy();

        assertThat(aITypePredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aITypePredictionCriteria)
        );
    }

    @Test
    void aITypePredictionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aITypePredictionCriteria = new AITypePredictionCriteria();
        setAllFilters(aITypePredictionCriteria);

        var copy = aITypePredictionCriteria.copy();

        assertThat(aITypePredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aITypePredictionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aITypePredictionCriteria = new AITypePredictionCriteria();

        assertThat(aITypePredictionCriteria).hasToString("AITypePredictionCriteria{}");
    }

    private static void setAllFilters(AITypePredictionCriteria aITypePredictionCriteria) {
        aITypePredictionCriteria.id();
        aITypePredictionCriteria.documentTypeName();
        aITypePredictionCriteria.confidence();
        aITypePredictionCriteria.reason();
        aITypePredictionCriteria.modelVersion();
        aITypePredictionCriteria.predictionS3Key();
        aITypePredictionCriteria.isAccepted();
        aITypePredictionCriteria.acceptedBy();
        aITypePredictionCriteria.acceptedDate();
        aITypePredictionCriteria.predictionDate();
        aITypePredictionCriteria.jobId();
        aITypePredictionCriteria.distinct();
    }

    private static Condition<AITypePredictionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentTypeName()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getReason()) &&
                condition.apply(criteria.getModelVersion()) &&
                condition.apply(criteria.getPredictionS3Key()) &&
                condition.apply(criteria.getIsAccepted()) &&
                condition.apply(criteria.getAcceptedBy()) &&
                condition.apply(criteria.getAcceptedDate()) &&
                condition.apply(criteria.getPredictionDate()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AITypePredictionCriteria> copyFiltersAre(
        AITypePredictionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentTypeName(), copy.getDocumentTypeName()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getReason(), copy.getReason()) &&
                condition.apply(criteria.getModelVersion(), copy.getModelVersion()) &&
                condition.apply(criteria.getPredictionS3Key(), copy.getPredictionS3Key()) &&
                condition.apply(criteria.getIsAccepted(), copy.getIsAccepted()) &&
                condition.apply(criteria.getAcceptedBy(), copy.getAcceptedBy()) &&
                condition.apply(criteria.getAcceptedDate(), copy.getAcceptedDate()) &&
                condition.apply(criteria.getPredictionDate(), copy.getPredictionDate()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
