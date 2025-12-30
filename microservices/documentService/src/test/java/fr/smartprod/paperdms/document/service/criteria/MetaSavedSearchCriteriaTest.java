package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaSavedSearchCriteriaTest {

    @Test
    void newMetaSavedSearchCriteriaHasAllFiltersNullTest() {
        var metaSavedSearchCriteria = new MetaSavedSearchCriteria();
        assertThat(metaSavedSearchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaSavedSearchCriteriaFluentMethodsCreatesFiltersTest() {
        var metaSavedSearchCriteria = new MetaSavedSearchCriteria();

        setAllFilters(metaSavedSearchCriteria);

        assertThat(metaSavedSearchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaSavedSearchCriteriaCopyCreatesNullFilterTest() {
        var metaSavedSearchCriteria = new MetaSavedSearchCriteria();
        var copy = metaSavedSearchCriteria.copy();

        assertThat(metaSavedSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaSavedSearchCriteria)
        );
    }

    @Test
    void metaSavedSearchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaSavedSearchCriteria = new MetaSavedSearchCriteria();
        setAllFilters(metaSavedSearchCriteria);

        var copy = metaSavedSearchCriteria.copy();

        assertThat(metaSavedSearchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaSavedSearchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaSavedSearchCriteria = new MetaSavedSearchCriteria();

        assertThat(metaSavedSearchCriteria).hasToString("MetaSavedSearchCriteria{}");
    }

    private static void setAllFilters(MetaSavedSearchCriteria metaSavedSearchCriteria) {
        metaSavedSearchCriteria.id();
        metaSavedSearchCriteria.name();
        metaSavedSearchCriteria.isPublic();
        metaSavedSearchCriteria.isAlert();
        metaSavedSearchCriteria.alertFrequency();
        metaSavedSearchCriteria.userId();
        metaSavedSearchCriteria.createdDate();
        metaSavedSearchCriteria.distinct();
    }

    private static Condition<MetaSavedSearchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<MetaSavedSearchCriteria> copyFiltersAre(
        MetaSavedSearchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
