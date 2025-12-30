package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingDashboardCriteriaTest {

    @Test
    void newReportingDashboardCriteriaHasAllFiltersNullTest() {
        var reportingDashboardCriteria = new ReportingDashboardCriteria();
        assertThat(reportingDashboardCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingDashboardCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingDashboardCriteria = new ReportingDashboardCriteria();

        setAllFilters(reportingDashboardCriteria);

        assertThat(reportingDashboardCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingDashboardCriteriaCopyCreatesNullFilterTest() {
        var reportingDashboardCriteria = new ReportingDashboardCriteria();
        var copy = reportingDashboardCriteria.copy();

        assertThat(reportingDashboardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingDashboardCriteria)
        );
    }

    @Test
    void reportingDashboardCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingDashboardCriteria = new ReportingDashboardCriteria();
        setAllFilters(reportingDashboardCriteria);

        var copy = reportingDashboardCriteria.copy();

        assertThat(reportingDashboardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingDashboardCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingDashboardCriteria = new ReportingDashboardCriteria();

        assertThat(reportingDashboardCriteria).hasToString("ReportingDashboardCriteria{}");
    }

    private static void setAllFilters(ReportingDashboardCriteria reportingDashboardCriteria) {
        reportingDashboardCriteria.id();
        reportingDashboardCriteria.name();
        reportingDashboardCriteria.userId();
        reportingDashboardCriteria.isPublic();
        reportingDashboardCriteria.refreshInterval();
        reportingDashboardCriteria.isDefault();
        reportingDashboardCriteria.createdDate();
        reportingDashboardCriteria.widgetsId();
        reportingDashboardCriteria.distinct();
    }

    private static Condition<ReportingDashboardCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getRefreshInterval()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getWidgetsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingDashboardCriteria> copyFiltersAre(
        ReportingDashboardCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getRefreshInterval(), copy.getRefreshInterval()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getWidgetsId(), copy.getWidgetsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
