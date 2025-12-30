package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringSystemHealthCriteriaTest {

    @Test
    void newMonitoringSystemHealthCriteriaHasAllFiltersNullTest() {
        var monitoringSystemHealthCriteria = new MonitoringSystemHealthCriteria();
        assertThat(monitoringSystemHealthCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringSystemHealthCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringSystemHealthCriteria = new MonitoringSystemHealthCriteria();

        setAllFilters(monitoringSystemHealthCriteria);

        assertThat(monitoringSystemHealthCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringSystemHealthCriteriaCopyCreatesNullFilterTest() {
        var monitoringSystemHealthCriteria = new MonitoringSystemHealthCriteria();
        var copy = monitoringSystemHealthCriteria.copy();

        assertThat(monitoringSystemHealthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringSystemHealthCriteria)
        );
    }

    @Test
    void monitoringSystemHealthCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringSystemHealthCriteria = new MonitoringSystemHealthCriteria();
        setAllFilters(monitoringSystemHealthCriteria);

        var copy = monitoringSystemHealthCriteria.copy();

        assertThat(monitoringSystemHealthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringSystemHealthCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringSystemHealthCriteria = new MonitoringSystemHealthCriteria();

        assertThat(monitoringSystemHealthCriteria).hasToString("MonitoringSystemHealthCriteria{}");
    }

    private static void setAllFilters(MonitoringSystemHealthCriteria monitoringSystemHealthCriteria) {
        monitoringSystemHealthCriteria.id();
        monitoringSystemHealthCriteria.serviceName();
        monitoringSystemHealthCriteria.status();
        monitoringSystemHealthCriteria.version();
        monitoringSystemHealthCriteria.uptime();
        monitoringSystemHealthCriteria.cpuUsage();
        monitoringSystemHealthCriteria.memoryUsage();
        monitoringSystemHealthCriteria.errorRate();
        monitoringSystemHealthCriteria.lastCheck();
        monitoringSystemHealthCriteria.distinct();
    }

    private static Condition<MonitoringSystemHealthCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getServiceName()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getUptime()) &&
                condition.apply(criteria.getCpuUsage()) &&
                condition.apply(criteria.getMemoryUsage()) &&
                condition.apply(criteria.getErrorRate()) &&
                condition.apply(criteria.getLastCheck()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringSystemHealthCriteria> copyFiltersAre(
        MonitoringSystemHealthCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getServiceName(), copy.getServiceName()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getUptime(), copy.getUptime()) &&
                condition.apply(criteria.getCpuUsage(), copy.getCpuUsage()) &&
                condition.apply(criteria.getMemoryUsage(), copy.getMemoryUsage()) &&
                condition.apply(criteria.getErrorRate(), copy.getErrorRate()) &&
                condition.apply(criteria.getLastCheck(), copy.getLastCheck()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
