package fr.smartprod.paperdms.search.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SearchFacetCriteriaTest {

    @Test
    void newSearchFacetCriteriaHasAllFiltersNullTest() {
        var searchFacetCriteria = new SearchFacetCriteria();
        assertThat(searchFacetCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void searchFacetCriteriaFluentMethodsCreatesFiltersTest() {
        var searchFacetCriteria = new SearchFacetCriteria();

        setAllFilters(searchFacetCriteria);

        assertThat(searchFacetCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void searchFacetCriteriaCopyCreatesNullFilterTest() {
        var searchFacetCriteria = new SearchFacetCriteria();
        var copy = searchFacetCriteria.copy();

        assertThat(searchFacetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(searchFacetCriteria)
        );
    }

    @Test
    void searchFacetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var searchFacetCriteria = new SearchFacetCriteria();
        setAllFilters(searchFacetCriteria);

        var copy = searchFacetCriteria.copy();

        assertThat(searchFacetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(searchFacetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var searchFacetCriteria = new SearchFacetCriteria();

        assertThat(searchFacetCriteria).hasToString("SearchFacetCriteria{}");
    }

    private static void setAllFilters(SearchFacetCriteria searchFacetCriteria) {
        searchFacetCriteria.id();
        searchFacetCriteria.facetName();
        searchFacetCriteria.facetType();
        searchFacetCriteria.searchQueryId();
        searchFacetCriteria.distinct();
    }

    private static Condition<SearchFacetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFacetName()) &&
                condition.apply(criteria.getFacetType()) &&
                condition.apply(criteria.getSearchQueryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SearchFacetCriteria> copyFiltersAre(SearchFacetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFacetName(), copy.getFacetName()) &&
                condition.apply(criteria.getFacetType(), copy.getFacetType()) &&
                condition.apply(criteria.getSearchQueryId(), copy.getSearchQueryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
