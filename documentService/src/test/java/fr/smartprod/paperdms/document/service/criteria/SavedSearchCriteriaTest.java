package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SavedSearchCriteriaTest {

    @Test
    void newSavedSearchCriteriaHasAllFiltersNullTest() {
        var savedSearchCriteria = new SavedSearchCriteria();
        assertThat(savedSearchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void savedSearchCriteriaFluentMethodsCreatesFiltersTest() {
        var savedSearchCriteria = new SavedSearchCriteria();

        setAllFilters(savedSearchCriteria);

        assertThat(savedSearchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void savedSearchCriteriaCopyCreatesNullFilterTest() {
        var savedSearchCriteria = new SavedSearchCriteria();
        var copy = savedSearchCriteria.copy();

        assertThat(savedSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(savedSearchCriteria)
        );
    }

    @Test
    void savedSearchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var savedSearchCriteria = new SavedSearchCriteria();
        setAllFilters(savedSearchCriteria);

        var copy = savedSearchCriteria.copy();

        assertThat(savedSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(savedSearchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var savedSearchCriteria = new SavedSearchCriteria();

        assertThat(savedSearchCriteria).hasToString("SavedSearchCriteria{}");
    }

    private static void setAllFilters(SavedSearchCriteria savedSearchCriteria) {
        savedSearchCriteria.id();
        savedSearchCriteria.name();
        savedSearchCriteria.isPublic();
        savedSearchCriteria.isAlert();
        savedSearchCriteria.alertFrequency();
        savedSearchCriteria.userId();
        savedSearchCriteria.createdDate();
        savedSearchCriteria.distinct();
    }

    private static Condition<SavedSearchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getIsAlert()) &&
                condition.apply(criteria.getAlertFrequency()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SavedSearchCriteria> copyFiltersAre(SavedSearchCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getIsAlert(), copy.getIsAlert()) &&
                condition.apply(criteria.getAlertFrequency(), copy.getAlertFrequency()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
