package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingPerformanceMetricCriteriaTest {

    @Test
    void newReportingPerformanceMetricCriteriaHasAllFiltersNullTest() {
        var reportingPerformanceMetricCriteria = new ReportingPerformanceMetricCriteria();
        assertThat(reportingPerformanceMetricCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingPerformanceMetricCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingPerformanceMetricCriteria = new ReportingPerformanceMetricCriteria();

        setAllFilters(reportingPerformanceMetricCriteria);

        assertThat(reportingPerformanceMetricCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingPerformanceMetricCriteriaCopyCreatesNullFilterTest() {
        var reportingPerformanceMetricCriteria = new ReportingPerformanceMetricCriteria();
        var copy = reportingPerformanceMetricCriteria.copy();

        assertThat(reportingPerformanceMetricCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingPerformanceMetricCriteria)
        );
    }

    @Test
    void reportingPerformanceMetricCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingPerformanceMetricCriteria = new ReportingPerformanceMetricCriteria();
        setAllFilters(reportingPerformanceMetricCriteria);

        var copy = reportingPerformanceMetricCriteria.copy();

        assertThat(reportingPerformanceMetricCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingPerformanceMetricCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingPerformanceMetricCriteria = new ReportingPerformanceMetricCriteria();

        assertThat(reportingPerformanceMetricCriteria).hasToString("ReportingPerformanceMetricCriteria{}");
    }

    private static void setAllFilters(ReportingPerformanceMetricCriteria reportingPerformanceMetricCriteria) {
        reportingPerformanceMetricCriteria.id();
        reportingPerformanceMetricCriteria.metricName();
        reportingPerformanceMetricCriteria.metricType();
        reportingPerformanceMetricCriteria.value();
        reportingPerformanceMetricCriteria.unit();
        reportingPerformanceMetricCriteria.serviceName();
        reportingPerformanceMetricCriteria.timestamp();
        reportingPerformanceMetricCriteria.distinct();
    }

    private static Condition<ReportingPerformanceMetricCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMetricName()) &&
                condition.apply(criteria.getMetricType()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getUnit()) &&
                condition.apply(criteria.getServiceName()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingPerformanceMetricCriteria> copyFiltersAre(
        ReportingPerformanceMetricCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMetricName(), copy.getMetricName()) &&
                condition.apply(criteria.getMetricType(), copy.getMetricType()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getUnit(), copy.getUnit()) &&
                condition.apply(criteria.getServiceName(), copy.getServiceName()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
