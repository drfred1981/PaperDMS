package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringServiceStatusCriteriaTest {

    @Test
    void newMonitoringServiceStatusCriteriaHasAllFiltersNullTest() {
        var monitoringServiceStatusCriteria = new MonitoringServiceStatusCriteria();
        assertThat(monitoringServiceStatusCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringServiceStatusCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringServiceStatusCriteria = new MonitoringServiceStatusCriteria();

        setAllFilters(monitoringServiceStatusCriteria);

        assertThat(monitoringServiceStatusCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringServiceStatusCriteriaCopyCreatesNullFilterTest() {
        var monitoringServiceStatusCriteria = new MonitoringServiceStatusCriteria();
        var copy = monitoringServiceStatusCriteria.copy();

        assertThat(monitoringServiceStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringServiceStatusCriteria)
        );
    }

    @Test
    void monitoringServiceStatusCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringServiceStatusCriteria = new MonitoringServiceStatusCriteria();
        setAllFilters(monitoringServiceStatusCriteria);

        var copy = monitoringServiceStatusCriteria.copy();

        assertThat(monitoringServiceStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringServiceStatusCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringServiceStatusCriteria = new MonitoringServiceStatusCriteria();

        assertThat(monitoringServiceStatusCriteria).hasToString("MonitoringServiceStatusCriteria{}");
    }

    private static void setAllFilters(MonitoringServiceStatusCriteria monitoringServiceStatusCriteria) {
        monitoringServiceStatusCriteria.id();
        monitoringServiceStatusCriteria.serviceName();
        monitoringServiceStatusCriteria.serviceType();
        monitoringServiceStatusCriteria.status();
        monitoringServiceStatusCriteria.endpoint();
        monitoringServiceStatusCriteria.port();
        monitoringServiceStatusCriteria.version();
        monitoringServiceStatusCriteria.lastPing();
        monitoringServiceStatusCriteria.isHealthy();
        monitoringServiceStatusCriteria.distinct();
    }

    private static Condition<MonitoringServiceStatusCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getServiceName()) &&
                condition.apply(criteria.getServiceType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getEndpoint()) &&
                condition.apply(criteria.getPort()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getLastPing()) &&
                condition.apply(criteria.getIsHealthy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringServiceStatusCriteria> copyFiltersAre(
        MonitoringServiceStatusCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getServiceName(), copy.getServiceName()) &&
                condition.apply(criteria.getServiceType(), copy.getServiceType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getEndpoint(), copy.getEndpoint()) &&
                condition.apply(criteria.getPort(), copy.getPort()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getLastPing(), copy.getLastPing()) &&
                condition.apply(criteria.getIsHealthy(), copy.getIsHealthy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
