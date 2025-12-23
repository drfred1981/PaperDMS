package fr.smartprod.paperdms.search.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SemanticSearchCriteriaTest {

    @Test
    void newSemanticSearchCriteriaHasAllFiltersNullTest() {
        var semanticSearchCriteria = new SemanticSearchCriteria();
        assertThat(semanticSearchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void semanticSearchCriteriaFluentMethodsCreatesFiltersTest() {
        var semanticSearchCriteria = new SemanticSearchCriteria();

        setAllFilters(semanticSearchCriteria);

        assertThat(semanticSearchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void semanticSearchCriteriaCopyCreatesNullFilterTest() {
        var semanticSearchCriteria = new SemanticSearchCriteria();
        var copy = semanticSearchCriteria.copy();

        assertThat(semanticSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(semanticSearchCriteria)
        );
    }

    @Test
    void semanticSearchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var semanticSearchCriteria = new SemanticSearchCriteria();
        setAllFilters(semanticSearchCriteria);

        var copy = semanticSearchCriteria.copy();

        assertThat(semanticSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(semanticSearchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var semanticSearchCriteria = new SemanticSearchCriteria();

        assertThat(semanticSearchCriteria).hasToString("SemanticSearchCriteria{}");
    }

    private static void setAllFilters(SemanticSearchCriteria semanticSearchCriteria) {
        semanticSearchCriteria.id();
        semanticSearchCriteria.query();
        semanticSearchCriteria.modelUsed();
        semanticSearchCriteria.executionTime();
        semanticSearchCriteria.userId();
        semanticSearchCriteria.searchDate();
        semanticSearchCriteria.distinct();
    }

    private static Condition<SemanticSearchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuery()) &&
                condition.apply(criteria.getModelUsed()) &&
                condition.apply(criteria.getExecutionTime()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getSearchDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SemanticSearchCriteria> copyFiltersAre(
        SemanticSearchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuery(), copy.getQuery()) &&
                condition.apply(criteria.getModelUsed(), copy.getModelUsed()) &&
                condition.apply(criteria.getExecutionTime(), copy.getExecutionTime()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getSearchDate(), copy.getSearchDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
