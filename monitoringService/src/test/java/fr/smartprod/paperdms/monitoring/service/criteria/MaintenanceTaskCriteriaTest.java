package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaintenanceTaskCriteriaTest {

    @Test
    void newMaintenanceTaskCriteriaHasAllFiltersNullTest() {
        var maintenanceTaskCriteria = new MaintenanceTaskCriteria();
        assertThat(maintenanceTaskCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void maintenanceTaskCriteriaFluentMethodsCreatesFiltersTest() {
        var maintenanceTaskCriteria = new MaintenanceTaskCriteria();

        setAllFilters(maintenanceTaskCriteria);

        assertThat(maintenanceTaskCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void maintenanceTaskCriteriaCopyCreatesNullFilterTest() {
        var maintenanceTaskCriteria = new MaintenanceTaskCriteria();
        var copy = maintenanceTaskCriteria.copy();

        assertThat(maintenanceTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceTaskCriteria)
        );
    }

    @Test
    void maintenanceTaskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var maintenanceTaskCriteria = new MaintenanceTaskCriteria();
        setAllFilters(maintenanceTaskCriteria);

        var copy = maintenanceTaskCriteria.copy();

        assertThat(maintenanceTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceTaskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var maintenanceTaskCriteria = new MaintenanceTaskCriteria();

        assertThat(maintenanceTaskCriteria).hasToString("MaintenanceTaskCriteria{}");
    }

    private static void setAllFilters(MaintenanceTaskCriteria maintenanceTaskCriteria) {
        maintenanceTaskCriteria.id();
        maintenanceTaskCriteria.name();
        maintenanceTaskCriteria.taskType();
        maintenanceTaskCriteria.schedule();
        maintenanceTaskCriteria.status();
        maintenanceTaskCriteria.isActive();
        maintenanceTaskCriteria.lastRun();
        maintenanceTaskCriteria.nextRun();
        maintenanceTaskCriteria.duration();
        maintenanceTaskCriteria.recordsProcessed();
        maintenanceTaskCriteria.createdBy();
        maintenanceTaskCriteria.createdDate();
        maintenanceTaskCriteria.distinct();
    }

    private static Condition<MaintenanceTaskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<MaintenanceTaskCriteria> copyFiltersAre(
        MaintenanceTaskCriteria copy,
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
