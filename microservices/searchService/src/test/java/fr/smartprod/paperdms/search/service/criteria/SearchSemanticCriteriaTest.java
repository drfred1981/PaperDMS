package fr.smartprod.paperdms.search.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SearchSemanticCriteriaTest {

    @Test
    void newSearchSemanticCriteriaHasAllFiltersNullTest() {
        var searchSemanticCriteria = new SearchSemanticCriteria();
        assertThat(searchSemanticCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void searchSemanticCriteriaFluentMethodsCreatesFiltersTest() {
        var searchSemanticCriteria = new SearchSemanticCriteria();

        setAllFilters(searchSemanticCriteria);

        assertThat(searchSemanticCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void searchSemanticCriteriaCopyCreatesNullFilterTest() {
        var searchSemanticCriteria = new SearchSemanticCriteria();
        var copy = searchSemanticCriteria.copy();

        assertThat(searchSemanticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(searchSemanticCriteria)
        );
    }

    @Test
    void searchSemanticCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var searchSemanticCriteria = new SearchSemanticCriteria();
        setAllFilters(searchSemanticCriteria);

        var copy = searchSemanticCriteria.copy();

        assertThat(searchSemanticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(searchSemanticCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var searchSemanticCriteria = new SearchSemanticCriteria();

        assertThat(searchSemanticCriteria).hasToString("SearchSemanticCriteria{}");
    }

    private static void setAllFilters(SearchSemanticCriteria searchSemanticCriteria) {
        searchSemanticCriteria.id();
        searchSemanticCriteria.query();
        searchSemanticCriteria.modelUsed();
        searchSemanticCriteria.executionTime();
        searchSemanticCriteria.userId();
        searchSemanticCriteria.searchDate();
        searchSemanticCriteria.distinct();
    }

    private static Condition<SearchSemanticCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SearchSemanticCriteria> copyFiltersAre(
        SearchSemanticCriteria copy,
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
