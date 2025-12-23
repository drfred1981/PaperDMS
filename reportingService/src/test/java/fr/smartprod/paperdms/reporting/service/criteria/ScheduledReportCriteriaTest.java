package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ScheduledReportCriteriaTest {

    @Test
    void newScheduledReportCriteriaHasAllFiltersNullTest() {
        var scheduledReportCriteria = new ScheduledReportCriteria();
        assertThat(scheduledReportCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void scheduledReportCriteriaFluentMethodsCreatesFiltersTest() {
        var scheduledReportCriteria = new ScheduledReportCriteria();

        setAllFilters(scheduledReportCriteria);

        assertThat(scheduledReportCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void scheduledReportCriteriaCopyCreatesNullFilterTest() {
        var scheduledReportCriteria = new ScheduledReportCriteria();
        var copy = scheduledReportCriteria.copy();

        assertThat(scheduledReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduledReportCriteria)
        );
    }

    @Test
    void scheduledReportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var scheduledReportCriteria = new ScheduledReportCriteria();
        setAllFilters(scheduledReportCriteria);

        var copy = scheduledReportCriteria.copy();

        assertThat(scheduledReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(scheduledReportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var scheduledReportCriteria = new ScheduledReportCriteria();

        assertThat(scheduledReportCriteria).hasToString("ScheduledReportCriteria{}");
    }

    private static void setAllFilters(ScheduledReportCriteria scheduledReportCriteria) {
        scheduledReportCriteria.id();
        scheduledReportCriteria.name();
        scheduledReportCriteria.reportType();
        scheduledReportCriteria.schedule();
        scheduledReportCriteria.format();
        scheduledReportCriteria.isActive();
        scheduledReportCriteria.lastRun();
        scheduledReportCriteria.nextRun();
        scheduledReportCriteria.createdBy();
        scheduledReportCriteria.createdDate();
        scheduledReportCriteria.distinct();
    }

    private static Condition<ScheduledReportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getReportType()) &&
                condition.apply(criteria.getSchedule()) &&
                condition.apply(criteria.getFormat()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getLastRun()) &&
                condition.apply(criteria.getNextRun()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ScheduledReportCriteria> copyFiltersAre(
        ScheduledReportCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getReportType(), copy.getReportType()) &&
                condition.apply(criteria.getSchedule(), copy.getSchedule()) &&
                condition.apply(criteria.getFormat(), copy.getFormat()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getLastRun(), copy.getLastRun()) &&
                condition.apply(criteria.getNextRun(), copy.getNextRun()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
