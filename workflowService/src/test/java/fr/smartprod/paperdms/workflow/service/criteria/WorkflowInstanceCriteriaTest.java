package fr.smartprod.paperdms.workflow.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkflowInstanceCriteriaTest {

    @Test
    void newWorkflowInstanceCriteriaHasAllFiltersNullTest() {
        var workflowInstanceCriteria = new WorkflowInstanceCriteria();
        assertThat(workflowInstanceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void workflowInstanceCriteriaFluentMethodsCreatesFiltersTest() {
        var workflowInstanceCriteria = new WorkflowInstanceCriteria();

        setAllFilters(workflowInstanceCriteria);

        assertThat(workflowInstanceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void workflowInstanceCriteriaCopyCreatesNullFilterTest() {
        var workflowInstanceCriteria = new WorkflowInstanceCriteria();
        var copy = workflowInstanceCriteria.copy();

        assertThat(workflowInstanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowInstanceCriteria)
        );
    }

    @Test
    void workflowInstanceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workflowInstanceCriteria = new WorkflowInstanceCriteria();
        setAllFilters(workflowInstanceCriteria);

        var copy = workflowInstanceCriteria.copy();

        assertThat(workflowInstanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowInstanceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workflowInstanceCriteria = new WorkflowInstanceCriteria();

        assertThat(workflowInstanceCriteria).hasToString("WorkflowInstanceCriteria{}");
    }

    private static void setAllFilters(WorkflowInstanceCriteria workflowInstanceCriteria) {
        workflowInstanceCriteria.id();
        workflowInstanceCriteria.documentId();
        workflowInstanceCriteria.status();
        workflowInstanceCriteria.currentStepNumber();
        workflowInstanceCriteria.startDate();
        workflowInstanceCriteria.dueDate();
        workflowInstanceCriteria.completedDate();
        workflowInstanceCriteria.cancelledDate();
        workflowInstanceCriteria.priority();
        workflowInstanceCriteria.createdBy();
        workflowInstanceCriteria.workflowId();
        workflowInstanceCriteria.distinct();
    }

    private static Condition<WorkflowInstanceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCurrentStepNumber()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getCompletedDate()) &&
                condition.apply(criteria.getCancelledDate()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getWorkflowId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkflowInstanceCriteria> copyFiltersAre(
        WorkflowInstanceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCurrentStepNumber(), copy.getCurrentStepNumber()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getCompletedDate(), copy.getCompletedDate()) &&
                condition.apply(criteria.getCancelledDate(), copy.getCancelledDate()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getWorkflowId(), copy.getWorkflowId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
