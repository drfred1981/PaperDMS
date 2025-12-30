package fr.smartprod.paperdms.similarity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SimilarityDocumentFingerprintCriteriaTest {

    @Test
    void newSimilarityDocumentFingerprintCriteriaHasAllFiltersNullTest() {
        var similarityDocumentFingerprintCriteria = new SimilarityDocumentFingerprintCriteria();
        assertThat(similarityDocumentFingerprintCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void similarityDocumentFingerprintCriteriaFluentMethodsCreatesFiltersTest() {
        var similarityDocumentFingerprintCriteria = new SimilarityDocumentFingerprintCriteria();

        setAllFilters(similarityDocumentFingerprintCriteria);

        assertThat(similarityDocumentFingerprintCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void similarityDocumentFingerprintCriteriaCopyCreatesNullFilterTest() {
        var similarityDocumentFingerprintCriteria = new SimilarityDocumentFingerprintCriteria();
        var copy = similarityDocumentFingerprintCriteria.copy();

        assertThat(similarityDocumentFingerprintCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityDocumentFingerprintCriteria)
        );
    }

    @Test
    void similarityDocumentFingerprintCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var similarityDocumentFingerprintCriteria = new SimilarityDocumentFingerprintCriteria();
        setAllFilters(similarityDocumentFingerprintCriteria);

        var copy = similarityDocumentFingerprintCriteria.copy();

        assertThat(similarityDocumentFingerprintCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(similarityDocumentFingerprintCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var similarityDocumentFingerprintCriteria = new SimilarityDocumentFingerprintCriteria();

        assertThat(similarityDocumentFingerprintCriteria).hasToString("SimilarityDocumentFingerprintCriteria{}");
    }

    private static void setAllFilters(SimilarityDocumentFingerprintCriteria similarityDocumentFingerprintCriteria) {
        similarityDocumentFingerprintCriteria.id();
        similarityDocumentFingerprintCriteria.fingerprintType();
        similarityDocumentFingerprintCriteria.computedDate();
        similarityDocumentFingerprintCriteria.lastUpdated();
        similarityDocumentFingerprintCriteria.distinct();
    }

    private static Condition<SimilarityDocumentFingerprintCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFingerprintType()) &&
                condition.apply(criteria.getComputedDate()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SimilarityDocumentFingerprintCriteria> copyFiltersAre(
        SimilarityDocumentFingerprintCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFingerprintType(), copy.getFingerprintType()) &&
                condition.apply(criteria.getComputedDate(), copy.getComputedDate()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
