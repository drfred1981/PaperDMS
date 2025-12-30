package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AICorrespondentPredictionCriteriaTest {

    @Test
    void newAICorrespondentPredictionCriteriaHasAllFiltersNullTest() {
        var aICorrespondentPredictionCriteria = new AICorrespondentPredictionCriteria();
        assertThat(aICorrespondentPredictionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aICorrespondentPredictionCriteriaFluentMethodsCreatesFiltersTest() {
        var aICorrespondentPredictionCriteria = new AICorrespondentPredictionCriteria();

        setAllFilters(aICorrespondentPredictionCriteria);

        assertThat(aICorrespondentPredictionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aICorrespondentPredictionCriteriaCopyCreatesNullFilterTest() {
        var aICorrespondentPredictionCriteria = new AICorrespondentPredictionCriteria();
        var copy = aICorrespondentPredictionCriteria.copy();

        assertThat(aICorrespondentPredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aICorrespondentPredictionCriteria)
        );
    }

    @Test
    void aICorrespondentPredictionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aICorrespondentPredictionCriteria = new AICorrespondentPredictionCriteria();
        setAllFilters(aICorrespondentPredictionCriteria);

        var copy = aICorrespondentPredictionCriteria.copy();

        assertThat(aICorrespondentPredictionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aICorrespondentPredictionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aICorrespondentPredictionCriteria = new AICorrespondentPredictionCriteria();

        assertThat(aICorrespondentPredictionCriteria).hasToString("AICorrespondentPredictionCriteria{}");
    }

    private static void setAllFilters(AICorrespondentPredictionCriteria aICorrespondentPredictionCriteria) {
        aICorrespondentPredictionCriteria.id();
        aICorrespondentPredictionCriteria.correspondentName();
        aICorrespondentPredictionCriteria.name();
        aICorrespondentPredictionCriteria.email();
        aICorrespondentPredictionCriteria.phone();
        aICorrespondentPredictionCriteria.company();
        aICorrespondentPredictionCriteria.type();
        aICorrespondentPredictionCriteria.role();
        aICorrespondentPredictionCriteria.confidence();
        aICorrespondentPredictionCriteria.reason();
        aICorrespondentPredictionCriteria.modelVersion();
        aICorrespondentPredictionCriteria.predictionS3Key();
        aICorrespondentPredictionCriteria.isAccepted();
        aICorrespondentPredictionCriteria.acceptedBy();
        aICorrespondentPredictionCriteria.acceptedDate();
        aICorrespondentPredictionCriteria.predictionDate();
        aICorrespondentPredictionCriteria.jobId();
        aICorrespondentPredictionCriteria.distinct();
    }

    private static Condition<AICorrespondentPredictionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCorrespondentName()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getCompany()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getRole()) &&
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

    private static Condition<AICorrespondentPredictionCriteria> copyFiltersAre(
        AICorrespondentPredictionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCorrespondentName(), copy.getCorrespondentName()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getCompany(), copy.getCompany()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getRole(), copy.getRole()) &&
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
