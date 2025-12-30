package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringMaintenanceTaskCriteriaTest {

    @Test
    void newMonitoringMaintenanceTaskCriteriaHasAllFiltersNullTest() {
        var monitoringMaintenanceTaskCriteria = new MonitoringMaintenanceTaskCriteria();
        assertThat(monitoringMaintenanceTaskCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringMaintenanceTaskCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringMaintenanceTaskCriteria = new MonitoringMaintenanceTaskCriteria();

        setAllFilters(monitoringMaintenanceTaskCriteria);

        assertThat(monitoringMaintenanceTaskCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringMaintenanceTaskCriteriaCopyCreatesNullFilterTest() {
        var monitoringMaintenanceTaskCriteria = new MonitoringMaintenanceTaskCriteria();
        var copy = monitoringMaintenanceTaskCriteria.copy();

        assertThat(monitoringMaintenanceTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringMaintenanceTaskCriteria)
        );
    }

    @Test
    void monitoringMaintenanceTaskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringMaintenanceTaskCriteria = new MonitoringMaintenanceTaskCriteria();
        setAllFilters(monitoringMaintenanceTaskCriteria);

        var copy = monitoringMaintenanceTaskCriteria.copy();

        assertThat(monitoringMaintenanceTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringMaintenanceTaskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringMaintenanceTaskCriteria = new MonitoringMaintenanceTaskCriteria();

        assertThat(monitoringMaintenanceTaskCriteria).hasToString("MonitoringMaintenanceTaskCriteria{}");
    }

    private static void setAllFilters(MonitoringMaintenanceTaskCriteria monitoringMaintenanceTaskCriteria) {
        monitoringMaintenanceTaskCriteria.id();
        monitoringMaintenanceTaskCriteria.name();
        monitoringMaintenanceTaskCriteria.taskType();
        monitoringMaintenanceTaskCriteria.schedule();
        monitoringMaintenanceTaskCriteria.status();
        monitoringMaintenanceTaskCriteria.isActive();
        monitoringMaintenanceTaskCriteria.lastRun();
        monitoringMaintenanceTaskCriteria.nextRun();
        monitoringMaintenanceTaskCriteria.duration();
        monitoringMaintenanceTaskCriteria.recordsProcessed();
        monitoringMaintenanceTaskCriteria.createdBy();
        monitoringMaintenanceTaskCriteria.createdDate();
        monitoringMaintenanceTaskCriteria.distinct();
    }

    private static Condition<MonitoringMaintenanceTaskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTaskType()) &&
                condition.apply(criteria.getSchedule()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getLastRun()) &&
                condition.apply(criteria.getNextRun()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getRecordsProcessed()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringMaintenanceTaskCriteria> copyFiltersAre(
        MonitoringMaintenanceTaskCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTaskType(), copy.getTaskType()) &&
                condition.apply(criteria.getSchedule(), copy.getSchedule()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getLastRun(), copy.getLastRun()) &&
                condition.apply(criteria.getNextRun(), copy.getNextRun()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getRecordsProcessed(), copy.getRecordsProcessed()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
