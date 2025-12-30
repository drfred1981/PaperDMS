package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingScheduledReportCriteriaTest {

    @Test
    void newReportingScheduledReportCriteriaHasAllFiltersNullTest() {
        var reportingScheduledReportCriteria = new ReportingScheduledReportCriteria();
        assertThat(reportingScheduledReportCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingScheduledReportCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingScheduledReportCriteria = new ReportingScheduledReportCriteria();

        setAllFilters(reportingScheduledReportCriteria);

        assertThat(reportingScheduledReportCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingScheduledReportCriteriaCopyCreatesNullFilterTest() {
        var reportingScheduledReportCriteria = new ReportingScheduledReportCriteria();
        var copy = reportingScheduledReportCriteria.copy();

        assertThat(reportingScheduledReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingScheduledReportCriteria)
        );
    }

    @Test
    void reportingScheduledReportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingScheduledReportCriteria = new ReportingScheduledReportCriteria();
        setAllFilters(reportingScheduledReportCriteria);

        var copy = reportingScheduledReportCriteria.copy();

        assertThat(reportingScheduledReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingScheduledReportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingScheduledReportCriteria = new ReportingScheduledReportCriteria();

        assertThat(reportingScheduledReportCriteria).hasToString("ReportingScheduledReportCriteria{}");
    }

    private static void setAllFilters(ReportingScheduledReportCriteria reportingScheduledReportCriteria) {
        reportingScheduledReportCriteria.id();
        reportingScheduledReportCriteria.name();
        reportingScheduledReportCriteria.reportType();
        reportingScheduledReportCriteria.schedule();
        reportingScheduledReportCriteria.format();
        reportingScheduledReportCriteria.isActive();
        reportingScheduledReportCriteria.lastRun();
        reportingScheduledReportCriteria.nextRun();
        reportingScheduledReportCriteria.createdBy();
        reportingScheduledReportCriteria.createdDate();
        reportingScheduledReportCriteria.reportsExecutionsId();
        reportingScheduledReportCriteria.distinct();
    }

    private static Condition<ReportingScheduledReportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getReportsExecutionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingScheduledReportCriteria> copyFiltersAre(
        ReportingScheduledReportCriteria copy,
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
                condition.apply(criteria.getReportsExecutionsId(), copy.getReportsExecutionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
