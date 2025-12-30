package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingDashboardWidgetCriteriaTest {

    @Test
    void newReportingDashboardWidgetCriteriaHasAllFiltersNullTest() {
        var reportingDashboardWidgetCriteria = new ReportingDashboardWidgetCriteria();
        assertThat(reportingDashboardWidgetCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingDashboardWidgetCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingDashboardWidgetCriteria = new ReportingDashboardWidgetCriteria();

        setAllFilters(reportingDashboardWidgetCriteria);

        assertThat(reportingDashboardWidgetCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingDashboardWidgetCriteriaCopyCreatesNullFilterTest() {
        var reportingDashboardWidgetCriteria = new ReportingDashboardWidgetCriteria();
        var copy = reportingDashboardWidgetCriteria.copy();

        assertThat(reportingDashboardWidgetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingDashboardWidgetCriteria)
        );
    }

    @Test
    void reportingDashboardWidgetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingDashboardWidgetCriteria = new ReportingDashboardWidgetCriteria();
        setAllFilters(reportingDashboardWidgetCriteria);

        var copy = reportingDashboardWidgetCriteria.copy();

        assertThat(reportingDashboardWidgetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingDashboardWidgetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingDashboardWidgetCriteria = new ReportingDashboardWidgetCriteria();

        assertThat(reportingDashboardWidgetCriteria).hasToString("ReportingDashboardWidgetCriteria{}");
    }

    private static void setAllFilters(ReportingDashboardWidgetCriteria reportingDashboardWidgetCriteria) {
        reportingDashboardWidgetCriteria.id();
        reportingDashboardWidgetCriteria.widgetType();
        reportingDashboardWidgetCriteria.title();
        reportingDashboardWidgetCriteria.dataSource();
        reportingDashboardWidgetCriteria.position();
        reportingDashboardWidgetCriteria.sizeX();
        reportingDashboardWidgetCriteria.sizeY();
        reportingDashboardWidgetCriteria.dashboarId();
        reportingDashboardWidgetCriteria.distinct();
    }

    private static Condition<ReportingDashboardWidgetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getWidgetType()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDataSource()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getSizeX()) &&
                condition.apply(criteria.getSizeY()) &&
                condition.apply(criteria.getDashboarId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingDashboardWidgetCriteria> copyFiltersAre(
        ReportingDashboardWidgetCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getWidgetType(), copy.getWidgetType()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDataSource(), copy.getDataSource()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getSizeX(), copy.getSizeX()) &&
                condition.apply(criteria.getSizeY(), copy.getSizeY()) &&
                condition.apply(criteria.getDashboarId(), copy.getDashboarId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
