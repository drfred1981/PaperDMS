package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringAlertCriteriaTest {

    @Test
    void newMonitoringAlertCriteriaHasAllFiltersNullTest() {
        var monitoringAlertCriteria = new MonitoringAlertCriteria();
        assertThat(monitoringAlertCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringAlertCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringAlertCriteria = new MonitoringAlertCriteria();

        setAllFilters(monitoringAlertCriteria);

        assertThat(monitoringAlertCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringAlertCriteriaCopyCreatesNullFilterTest() {
        var monitoringAlertCriteria = new MonitoringAlertCriteria();
        var copy = monitoringAlertCriteria.copy();

        assertThat(monitoringAlertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringAlertCriteria)
        );
    }

    @Test
    void monitoringAlertCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringAlertCriteria = new MonitoringAlertCriteria();
        setAllFilters(monitoringAlertCriteria);

        var copy = monitoringAlertCriteria.copy();

        assertThat(monitoringAlertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringAlertCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringAlertCriteria = new MonitoringAlertCriteria();

        assertThat(monitoringAlertCriteria).hasToString("MonitoringAlertCriteria{}");
    }

    private static void setAllFilters(MonitoringAlertCriteria monitoringAlertCriteria) {
        monitoringAlertCriteria.id();
        monitoringAlertCriteria.severity();
        monitoringAlertCriteria.title();
        monitoringAlertCriteria.entityType();
        monitoringAlertCriteria.entityName();
        monitoringAlertCriteria.status();
        monitoringAlertCriteria.triggeredDate();
        monitoringAlertCriteria.acknowledgedBy();
        monitoringAlertCriteria.acknowledgedDate();
        monitoringAlertCriteria.resolvedBy();
        monitoringAlertCriteria.resolvedDate();
        monitoringAlertCriteria.alertRuleId();
        monitoringAlertCriteria.distinct();
    }

    private static Condition<MonitoringAlertCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSeverity()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getEntityType()) &&
                condition.apply(criteria.getEntityName()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTriggeredDate()) &&
                condition.apply(criteria.getAcknowledgedBy()) &&
                condition.apply(criteria.getAcknowledgedDate()) &&
                condition.apply(criteria.getResolvedBy()) &&
                condition.apply(criteria.getResolvedDate()) &&
                condition.apply(criteria.getAlertRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringAlertCriteria> copyFiltersAre(
        MonitoringAlertCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSeverity(), copy.getSeverity()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getEntityType(), copy.getEntityType()) &&
                condition.apply(criteria.getEntityName(), copy.getEntityName()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTriggeredDate(), copy.getTriggeredDate()) &&
                condition.apply(criteria.getAcknowledgedBy(), copy.getAcknowledgedBy()) &&
                condition.apply(criteria.getAcknowledgedDate(), copy.getAcknowledgedDate()) &&
                condition.apply(criteria.getResolvedBy(), copy.getResolvedBy()) &&
                condition.apply(criteria.getResolvedDate(), copy.getResolvedDate()) &&
                condition.apply(criteria.getAlertRuleId(), copy.getAlertRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
