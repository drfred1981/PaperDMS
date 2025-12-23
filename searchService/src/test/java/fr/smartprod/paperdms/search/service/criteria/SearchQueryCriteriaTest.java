package fr.smartprod.paperdms.search.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SearchQueryCriteriaTest {

    @Test
    void newSearchQueryCriteriaHasAllFiltersNullTest() {
        var searchQueryCriteria = new SearchQueryCriteria();
        assertThat(searchQueryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void searchQueryCriteriaFluentMethodsCreatesFiltersTest() {
        var searchQueryCriteria = new SearchQueryCriteria();

        setAllFilters(searchQueryCriteria);

        assertThat(searchQueryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void searchQueryCriteriaCopyCreatesNullFilterTest() {
        var searchQueryCriteria = new SearchQueryCriteria();
        var copy = searchQueryCriteria.copy();

        assertThat(searchQueryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(searchQueryCriteria)
        );
    }

    @Test
    void searchQueryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var searchQueryCriteria = new SearchQueryCriteria();
        setAllFilters(searchQueryCriteria);

        var copy = searchQueryCriteria.copy();

        assertThat(searchQueryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(searchQueryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var searchQueryCriteria = new SearchQueryCriteria();

        assertThat(searchQueryCriteria).hasToString("SearchQueryCriteria{}");
    }

    private static void setAllFilters(SearchQueryCriteria searchQueryCriteria) {
        searchQueryCriteria.id();
        searchQueryCriteria.query();
        searchQueryCriteria.resultCount();
        searchQueryCriteria.executionTime();
        searchQueryCriteria.userId();
        searchQueryCriteria.searchDate();
        searchQueryCriteria.isRelevant();
        searchQueryCriteria.distinct();
    }

    private static Condition<SearchQueryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuery()) &&
                condition.apply(criteria.getResultCount()) &&
                condition.apply(criteria.getExecutionTime()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getSearchDate()) &&
                condition.apply(criteria.getIsRelevant()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SearchQueryCriteria> copyFiltersAre(SearchQueryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuery(), copy.getQuery()) &&
                condition.apply(criteria.getResultCount(), copy.getResultCount()) &&
                condition.apply(criteria.getExecutionTime(), copy.getExecutionTime()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getSearchDate(), copy.getSearchDate()) &&
                condition.apply(criteria.getIsRelevant(), copy.getIsRelevant()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
