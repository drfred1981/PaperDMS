package fr.smartprod.paperdms.reporting.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportingExecutionCriteriaTest {

    @Test
    void newReportingExecutionCriteriaHasAllFiltersNullTest() {
        var reportingExecutionCriteria = new ReportingExecutionCriteria();
        assertThat(reportingExecutionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportingExecutionCriteriaFluentMethodsCreatesFiltersTest() {
        var reportingExecutionCriteria = new ReportingExecutionCriteria();

        setAllFilters(reportingExecutionCriteria);

        assertThat(reportingExecutionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportingExecutionCriteriaCopyCreatesNullFilterTest() {
        var reportingExecutionCriteria = new ReportingExecutionCriteria();
        var copy = reportingExecutionCriteria.copy();

        assertThat(reportingExecutionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingExecutionCriteria)
        );
    }

    @Test
    void reportingExecutionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportingExecutionCriteria = new ReportingExecutionCriteria();
        setAllFilters(reportingExecutionCriteria);

        var copy = reportingExecutionCriteria.copy();

        assertThat(reportingExecutionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportingExecutionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportingExecutionCriteria = new ReportingExecutionCriteria();

        assertThat(reportingExecutionCriteria).hasToString("ReportingExecutionCriteria{}");
    }

    private static void setAllFilters(ReportingExecutionCriteria reportingExecutionCriteria) {
        reportingExecutionCriteria.id();
        reportingExecutionCriteria.status();
        reportingExecutionCriteria.startDate();
        reportingExecutionCriteria.endDate();
        reportingExecutionCriteria.recordsProcessed();
        reportingExecutionCriteria.outputS3Key();
        reportingExecutionCriteria.outputSize();
        reportingExecutionCriteria.scheduledReportId();
        reportingExecutionCriteria.distinct();
    }

    private static Condition<ReportingExecutionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getRecordsProcessed()) &&
                condition.apply(criteria.getOutputS3Key()) &&
                condition.apply(criteria.getOutputSize()) &&
                condition.apply(criteria.getScheduledReportId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportingExecutionCriteria> copyFiltersAre(
        ReportingExecutionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getRecordsProcessed(), copy.getRecordsProcessed()) &&
                condition.apply(criteria.getOutputS3Key(), copy.getOutputS3Key()) &&
                condition.apply(criteria.getOutputSize(), copy.getOutputSize()) &&
                condition.apply(criteria.getScheduledReportId(), copy.getScheduledReportId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
