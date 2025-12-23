package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DashboardCriteriaTest {

    @Test
    void newDashboardCriteriaHasAllFiltersNullTest() {
        var dashboardCriteria = new DashboardCriteria();
        assertThat(dashboardCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dashboardCriteriaFluentMethodsCreatesFiltersTest() {
        var dashboardCriteria = new DashboardCriteria();

        setAllFilters(dashboardCriteria);

        assertThat(dashboardCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dashboardCriteriaCopyCreatesNullFilterTest() {
        var dashboardCriteria = new DashboardCriteria();
        var copy = dashboardCriteria.copy();

        assertThat(dashboardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dashboardCriteria)
        );
    }

    @Test
    void dashboardCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dashboardCriteria = new DashboardCriteria();
        setAllFilters(dashboardCriteria);

        var copy = dashboardCriteria.copy();

        assertThat(dashboardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dashboardCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dashboardCriteria = new DashboardCriteria();

        assertThat(dashboardCriteria).hasToString("DashboardCriteria{}");
    }

    private static void setAllFilters(DashboardCriteria dashboardCriteria) {
        dashboardCriteria.id();
        dashboardCriteria.name();
        dashboardCriteria.userId();
        dashboardCriteria.isPublic();
        dashboardCriteria.refreshInterval();
        dashboardCriteria.isDefault();
        dashboardCriteria.createdDate();
        dashboardCriteria.distinct();
    }

    private static Condition<DashboardCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getRefreshInterval()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DashboardCriteria> copyFiltersAre(DashboardCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getRefreshInterval(), copy.getRefreshInterval()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
