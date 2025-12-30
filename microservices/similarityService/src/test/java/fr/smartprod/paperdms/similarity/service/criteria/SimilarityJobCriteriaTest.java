package fr.smartprod.paperdms.similarity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SimilarityJobCriteriaTest {

    @Test
    void newSimilarityJobCriteriaHasAllFiltersNullTest() {
        var similarityJobCriteria = new SimilarityJobCriteria();
        assertThat(similarityJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void similarityJobCriteriaFluentMethodsCreatesFiltersTest() {
        var similarityJobCriteria = new SimilarityJobCriteria();

        setAllFilters(similarityJobCriteria);

        assertThat(similarityJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void similarityJobCriteriaCopyCreatesNullFilterTest() {
        var similarityJobCriteria = new SimilarityJobCriteria();
        var copy = similarityJobCriteria.copy();

        assertThat(similarityJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityJobCriteria)
        );
    }

    @Test
    void similarityJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var similarityJobCriteria = new SimilarityJobCriteria();
        setAllFilters(similarityJobCriteria);

        var copy = similarityJobCriteria.copy();

        assertThat(similarityJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var similarityJobCriteria = new SimilarityJobCriteria();

        assertThat(similarityJobCriteria).hasToString("SimilarityJobCriteria{}");
    }

    private static void setAllFilters(SimilarityJobCriteria similarityJobCriteria) {
        similarityJobCriteria.id();
        similarityJobCriteria.documentSha256();
        similarityJobCriteria.status();
        similarityJobCriteria.algorithm();
        similarityJobCriteria.scope();
        similarityJobCriteria.minSimilarityThreshold();
        similarityJobCriteria.matchesFound();
        similarityJobCriteria.startDate();
        similarityJobCriteria.endDate();
        similarityJobCriteria.createdDate();
        similarityJobCriteria.createdBy();
        similarityJobCriteria.similaritiesId();
        similarityJobCriteria.distinct();
    }

    private static Condition<SimilarityJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAlgorithm()) &&
                condition.apply(criteria.getScope()) &&
                condition.apply(criteria.getMinSimilarityThreshold()) &&
                condition.apply(criteria.getMatchesFound()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getSimilaritiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SimilarityJobCriteria> copyFiltersAre(
        SimilarityJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAlgorithm(), copy.getAlgorithm()) &&
                condition.apply(criteria.getScope(), copy.getScope()) &&
                condition.apply(criteria.getMinSimilarityThreshold(), copy.getMinSimilarityThreshold()) &&
                condition.apply(criteria.getMatchesFound(), copy.getMatchesFound()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getSimilaritiesId(), copy.getSimilaritiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
