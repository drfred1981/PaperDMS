package fr.smartprod.paperdms.similarity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SimilarityDocumentComparisonCriteriaTest {

    @Test
    void newSimilarityDocumentComparisonCriteriaHasAllFiltersNullTest() {
        var similarityDocumentComparisonCriteria = new SimilarityDocumentComparisonCriteria();
        assertThat(similarityDocumentComparisonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void similarityDocumentComparisonCriteriaFluentMethodsCreatesFiltersTest() {
        var similarityDocumentComparisonCriteria = new SimilarityDocumentComparisonCriteria();

        setAllFilters(similarityDocumentComparisonCriteria);

        assertThat(similarityDocumentComparisonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void similarityDocumentComparisonCriteriaCopyCreatesNullFilterTest() {
        var similarityDocumentComparisonCriteria = new SimilarityDocumentComparisonCriteria();
        var copy = similarityDocumentComparisonCriteria.copy();

        assertThat(similarityDocumentComparisonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityDocumentComparisonCriteria)
        );
    }

    @Test
    void similarityDocumentComparisonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var similarityDocumentComparisonCriteria = new SimilarityDocumentComparisonCriteria();
        setAllFilters(similarityDocumentComparisonCriteria);

        var copy = similarityDocumentComparisonCriteria.copy();

        assertThat(similarityDocumentComparisonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityDocumentComparisonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var similarityDocumentComparisonCriteria = new SimilarityDocumentComparisonCriteria();

        assertThat(similarityDocumentComparisonCriteria).hasToString("SimilarityDocumentComparisonCriteria{}");
    }

    private static void setAllFilters(SimilarityDocumentComparisonCriteria similarityDocumentComparisonCriteria) {
        similarityDocumentComparisonCriteria.id();
        similarityDocumentComparisonCriteria.sourceDocumentSha256();
        similarityDocumentComparisonCriteria.targetDocumentSha256();
        similarityDocumentComparisonCriteria.similarityScore();
        similarityDocumentComparisonCriteria.algorithm();
        similarityDocumentComparisonCriteria.computedDate();
        similarityDocumentComparisonCriteria.isRelevant();
        similarityDocumentComparisonCriteria.reviewedBy();
        similarityDocumentComparisonCriteria.reviewedDate();
        similarityDocumentComparisonCriteria.jobId();
        similarityDocumentComparisonCriteria.distinct();
    }

    private static Condition<SimilarityDocumentComparisonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSourceDocumentSha256()) &&
                condition.apply(criteria.getTargetDocumentSha256()) &&
                condition.apply(criteria.getSimilarityScore()) &&
                condition.apply(criteria.getAlgorithm()) &&
                condition.apply(criteria.getComputedDate()) &&
                condition.apply(criteria.getIsRelevant()) &&
                condition.apply(criteria.getReviewedBy()) &&
                condition.apply(criteria.getReviewedDate()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SimilarityDocumentComparisonCriteria> copyFiltersAre(
        SimilarityDocumentComparisonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSourceDocumentSha256(), copy.getSourceDocumentSha256()) &&
                condition.apply(criteria.getTargetDocumentSha256(), copy.getTargetDocumentSha256()) &&
                condition.apply(criteria.getSimilarityScore(), copy.getSimilarityScore()) &&
                condition.apply(criteria.getAlgorithm(), copy.getAlgorithm()) &&
                condition.apply(criteria.getComputedDate(), copy.getComputedDate()) &&
                condition.apply(criteria.getIsRelevant(), copy.getIsRelevant()) &&
                condition.apply(criteria.getReviewedBy(), copy.getReviewedBy()) &&
                condition.apply(criteria.getReviewedDate(), copy.getReviewedDate()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
