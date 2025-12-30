package fr.smartprod.paperdms.search.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SearchIndexCriteriaTest {

    @Test
    void newSearchIndexCriteriaHasAllFiltersNullTest() {
        var searchIndexCriteria = new SearchIndexCriteria();
        assertThat(searchIndexCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void searchIndexCriteriaFluentMethodsCreatesFiltersTest() {
        var searchIndexCriteria = new SearchIndexCriteria();

        setAllFilters(searchIndexCriteria);

        assertThat(searchIndexCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void searchIndexCriteriaCopyCreatesNullFilterTest() {
        var searchIndexCriteria = new SearchIndexCriteria();
        var copy = searchIndexCriteria.copy();

        assertThat(searchIndexCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(searchIndexCriteria)
        );
    }

    @Test
    void searchIndexCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var searchIndexCriteria = new SearchIndexCriteria();
        setAllFilters(searchIndexCriteria);

        var copy = searchIndexCriteria.copy();

        assertThat(searchIndexCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(searchIndexCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var searchIndexCriteria = new SearchIndexCriteria();

        assertThat(searchIndexCriteria).hasToString("SearchIndexCriteria{}");
    }

    private static void setAllFilters(SearchIndexCriteria searchIndexCriteria) {
        searchIndexCriteria.id();
        searchIndexCriteria.documentSha256();
        searchIndexCriteria.tags();
        searchIndexCriteria.correspondents();
        searchIndexCriteria.indexedDate();
        searchIndexCriteria.lastUpdated();
        searchIndexCriteria.distinct();
    }

    private static Condition<SearchIndexCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getTags()) &&
                condition.apply(criteria.getCorrespondents()) &&
                condition.apply(criteria.getIndexedDate()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SearchIndexCriteria> copyFiltersAre(SearchIndexCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getTags(), copy.getTags()) &&
                condition.apply(criteria.getCorrespondents(), copy.getCorrespondents()) &&
                condition.apply(criteria.getIndexedDate(), copy.getIndexedDate()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
