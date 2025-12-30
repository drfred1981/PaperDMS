package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AITagPredictionCriteriaTest {

    @Test
    void newAITagPredictionCriteriaHasAllFiltersNullTest() {
        var aITagPredictionCriteria = new AITagPredictionCriteria();
        assertThat(aITagPredictionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aITagPredictionCriteriaFluentMethodsCreatesFiltersTest() {
        var aITagPredictionCriteria = new AITagPredictionCriteria();

        setAllFilters(aITagPredictionCriteria);

        assertThat(aITagPredictionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aITagPredictionCriteriaCopyCreatesNullFilterTest() {
        var aITagPredictionCriteria = new AITagPredictionCriteria();
        var copy = aITagPredictionCriteria.copy();

        assertThat(aITagPredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aITagPredictionCriteria)
        );
    }

    @Test
    void aITagPredictionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aITagPredictionCriteria = new AITagPredictionCriteria();
        setAllFilters(aITagPredictionCriteria);

        var copy = aITagPredictionCriteria.copy();

        assertThat(aITagPredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aITagPredictionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aITagPredictionCriteria = new AITagPredictionCriteria();

        assertThat(aITagPredictionCriteria).hasToString("AITagPredictionCriteria{}");
    }

    private static void setAllFilters(AITagPredictionCriteria aITagPredictionCriteria) {
        aITagPredictionCriteria.id();
        aITagPredictionCriteria.tagName();
        aITagPredictionCriteria.confidence();
        aITagPredictionCriteria.reason();
        aITagPredictionCriteria.modelVersion();
        aITagPredictionCriteria.predictionS3Key();
        aITagPredictionCriteria.isAccepted();
        aITagPredictionCriteria.acceptedBy();
        aITagPredictionCriteria.acceptedDate();
        aITagPredictionCriteria.predictionDate();
        aITagPredictionCriteria.jobId();
        aITagPredictionCriteria.distinct();
    }

    private static Condition<AITagPredictionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTagName()) &&
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

    private static Condition<AITagPredictionCriteria> copyFiltersAre(
        AITagPredictionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTagName(), copy.getTagName()) &&
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
