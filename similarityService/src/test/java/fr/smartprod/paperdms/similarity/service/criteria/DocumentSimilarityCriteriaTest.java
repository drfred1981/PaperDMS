package fr.smartprod.paperdms.similarity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentSimilarityCriteriaTest {

    @Test
    void newDocumentSimilarityCriteriaHasAllFiltersNullTest() {
        var documentSimilarityCriteria = new DocumentSimilarityCriteria();
        assertThat(documentSimilarityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentSimilarityCriteriaFluentMethodsCreatesFiltersTest() {
        var documentSimilarityCriteria = new DocumentSimilarityCriteria();

        setAllFilters(documentSimilarityCriteria);

        assertThat(documentSimilarityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentSimilarityCriteriaCopyCreatesNullFilterTest() {
        var documentSimilarityCriteria = new DocumentSimilarityCriteria();
        var copy = documentSimilarityCriteria.copy();

        assertThat(documentSimilarityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentSimilarityCriteria)
        );
    }

    @Test
    void documentSimilarityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentSimilarityCriteria = new DocumentSimilarityCriteria();
        setAllFilters(documentSimilarityCriteria);

        var copy = documentSimilarityCriteria.copy();

        assertThat(documentSimilarityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentSimilarityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentSimilarityCriteria = new DocumentSimilarityCriteria();

        assertThat(documentSimilarityCriteria).hasToString("DocumentSimilarityCriteria{}");
    }

    private static void setAllFilters(DocumentSimilarityCriteria documentSimilarityCriteria) {
        documentSimilarityCriteria.id();
        documentSimilarityCriteria.documentId1();
        documentSimilarityCriteria.documentId2();
        documentSimilarityCriteria.similarityScore();
        documentSimilarityCriteria.algorithm();
        documentSimilarityCriteria.computedDate();
        documentSimilarityCriteria.isRelevant();
        documentSimilarityCriteria.reviewedBy();
        documentSimilarityCriteria.reviewedDate();
        documentSimilarityCriteria.jobId();
        documentSimilarityCriteria.distinct();
    }

    private static Condition<DocumentSimilarityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId1()) &&
                condition.apply(criteria.getDocumentId2()) &&
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

    private static Condition<DocumentSimilarityCriteria> copyFiltersAre(
        DocumentSimilarityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId1(), copy.getDocumentId1()) &&
                condition.apply(criteria.getDocumentId2(), copy.getDocumentId2()) &&
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
