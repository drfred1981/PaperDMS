package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringAlertRuleCriteriaTest {

    @Test
    void newMonitoringAlertRuleCriteriaHasAllFiltersNullTest() {
        var monitoringAlertRuleCriteria = new MonitoringAlertRuleCriteria();
        assertThat(monitoringAlertRuleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringAlertRuleCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringAlertRuleCriteria = new MonitoringAlertRuleCriteria();

        setAllFilters(monitoringAlertRuleCriteria);

        assertThat(monitoringAlertRuleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringAlertRuleCriteriaCopyCreatesNullFilterTest() {
        var monitoringAlertRuleCriteria = new MonitoringAlertRuleCriteria();
        var copy = monitoringAlertRuleCriteria.copy();

        assertThat(monitoringAlertRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringAlertRuleCriteria)
        );
    }

    @Test
    void monitoringAlertRuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringAlertRuleCriteria = new MonitoringAlertRuleCriteria();
        setAllFilters(monitoringAlertRuleCriteria);

        var copy = monitoringAlertRuleCriteria.copy();

        assertThat(monitoringAlertRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringAlertRuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringAlertRuleCriteria = new MonitoringAlertRuleCriteria();

        assertThat(monitoringAlertRuleCriteria).hasToString("MonitoringAlertRuleCriteria{}");
    }

    private static void setAllFilters(MonitoringAlertRuleCriteria monitoringAlertRuleCriteria) {
        monitoringAlertRuleCriteria.id();
        monitoringAlertRuleCriteria.name();
        monitoringAlertRuleCriteria.alertType();
        monitoringAlertRuleCriteria.severity();
        monitoringAlertRuleCriteria.isActive();
        monitoringAlertRuleCriteria.triggerCount();
        monitoringAlertRuleCriteria.lastTriggered();
        monitoringAlertRuleCriteria.createdBy();
        monitoringAlertRuleCriteria.createdDate();
        monitoringAlertRuleCriteria.alertsId();
        monitoringAlertRuleCriteria.distinct();
    }

    private static Condition<MonitoringAlertRuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAlertType()) &&
                condition.apply(criteria.getSeverity()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTriggerCount()) &&
                condition.apply(criteria.getLastTriggered()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getAlertsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringAlertRuleCriteria> copyFiltersAre(
        MonitoringAlertRuleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAlertType(), copy.getAlertType()) &&
                condition.apply(criteria.getSeverity(), copy.getSeverity()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTriggerCount(), copy.getTriggerCount()) &&
                condition.apply(criteria.getLastTriggered(), copy.getLastTriggered()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getAlertsId(), copy.getAlertsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
