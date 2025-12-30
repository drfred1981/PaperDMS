package fr.smartprod.paperdms.similarity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SimilarityClusterCriteriaTest {

    @Test
    void newSimilarityClusterCriteriaHasAllFiltersNullTest() {
        var similarityClusterCriteria = new SimilarityClusterCriteria();
        assertThat(similarityClusterCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void similarityClusterCriteriaFluentMethodsCreatesFiltersTest() {
        var similarityClusterCriteria = new SimilarityClusterCriteria();

        setAllFilters(similarityClusterCriteria);

        assertThat(similarityClusterCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void similarityClusterCriteriaCopyCreatesNullFilterTest() {
        var similarityClusterCriteria = new SimilarityClusterCriteria();
        var copy = similarityClusterCriteria.copy();

        assertThat(similarityClusterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityClusterCriteria)
        );
    }

    @Test
    void similarityClusterCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var similarityClusterCriteria = new SimilarityClusterCriteria();
        setAllFilters(similarityClusterCriteria);

        var copy = similarityClusterCriteria.copy();

        assertThat(similarityClusterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityClusterCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var similarityClusterCriteria = new SimilarityClusterCriteria();

        assertThat(similarityClusterCriteria).hasToString("SimilarityClusterCriteria{}");
    }

    private static void setAllFilters(SimilarityClusterCriteria similarityClusterCriteria) {
        similarityClusterCriteria.id();
        similarityClusterCriteria.name();
        similarityClusterCriteria.algorithm();
        similarityClusterCriteria.documentCount();
        similarityClusterCriteria.avgSimilarity();
        similarityClusterCriteria.createdDate();
        similarityClusterCriteria.lastUpdated();
        similarityClusterCriteria.distinct();
    }

    private static Condition<SimilarityClusterCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAlgorithm()) &&
                condition.apply(criteria.getDocumentCount()) &&
                condition.apply(criteria.getAvgSimilarity()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SimilarityClusterCriteria> copyFiltersAre(
        SimilarityClusterCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAlgorithm(), copy.getAlgorithm()) &&
                condition.apply(criteria.getDocumentCount(), copy.getDocumentCount()) &&
                condition.apply(criteria.getAvgSimilarity(), copy.getAvgSimilarity()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
