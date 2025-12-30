package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingSystemMetricCriteriaTest {

    @Test
    void newReportingSystemMetricCriteriaHasAllFiltersNullTest() {
        var reportingSystemMetricCriteria = new ReportingSystemMetricCriteria();
        assertThat(reportingSystemMetricCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingSystemMetricCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingSystemMetricCriteria = new ReportingSystemMetricCriteria();

        setAllFilters(reportingSystemMetricCriteria);

        assertThat(reportingSystemMetricCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingSystemMetricCriteriaCopyCreatesNullFilterTest() {
        var reportingSystemMetricCriteria = new ReportingSystemMetricCriteria();
        var copy = reportingSystemMetricCriteria.copy();

        assertThat(reportingSystemMetricCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingSystemMetricCriteria)
        );
    }

    @Test
    void reportingSystemMetricCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingSystemMetricCriteria = new ReportingSystemMetricCriteria();
        setAllFilters(reportingSystemMetricCriteria);

        var copy = reportingSystemMetricCriteria.copy();

        assertThat(reportingSystemMetricCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingSystemMetricCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingSystemMetricCriteria = new ReportingSystemMetricCriteria();

        assertThat(reportingSystemMetricCriteria).hasToString("ReportingSystemMetricCriteria{}");
    }

    private static void setAllFilters(ReportingSystemMetricCriteria reportingSystemMetricCriteria) {
        reportingSystemMetricCriteria.id();
        reportingSystemMetricCriteria.metricName();
        reportingSystemMetricCriteria.cpuUsage();
        reportingSystemMetricCriteria.memoryUsage();
        reportingSystemMetricCriteria.diskUsage();
        reportingSystemMetricCriteria.networkIn();
        reportingSystemMetricCriteria.networkOut();
        reportingSystemMetricCriteria.activeConnections();
        reportingSystemMetricCriteria.timestamp();
        reportingSystemMetricCriteria.distinct();
    }

    private static Condition<ReportingSystemMetricCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMetricName()) &&
                condition.apply(criteria.getCpuUsage()) &&
                condition.apply(criteria.getMemoryUsage()) &&
                condition.apply(criteria.getDiskUsage()) &&
                condition.apply(criteria.getNetworkIn()) &&
                condition.apply(criteria.getNetworkOut()) &&
                condition.apply(criteria.getActiveConnections()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingSystemMetricCriteria> copyFiltersAre(
        ReportingSystemMetricCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMetricName(), copy.getMetricName()) &&
                condition.apply(criteria.getCpuUsage(), copy.getCpuUsage()) &&
                condition.apply(criteria.getMemoryUsage(), copy.getMemoryUsage()) &&
                condition.apply(criteria.getDiskUsage(), copy.getDiskUsage()) &&
                condition.apply(criteria.getNetworkIn(), copy.getNetworkIn()) &&
                condition.apply(criteria.getNetworkOut(), copy.getNetworkOut()) &&
                condition.apply(criteria.getActiveConnections(), copy.getActiveConnections()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
