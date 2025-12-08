package com.ged.workflow.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkflowTaskCriteriaTest {

    @Test
    void newWorkflowTaskCriteriaHasAllFiltersNullTest() {
        var workflowTaskCriteria = new WorkflowTaskCriteria();
        assertThat(workflowTaskCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void workflowTaskCriteriaFluentMethodsCreatesFiltersTest() {
        var workflowTaskCriteria = new WorkflowTaskCriteria();

        setAllFilters(workflowTaskCriteria);

        assertThat(workflowTaskCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void workflowTaskCriteriaCopyCreatesNullFilterTest() {
        var workflowTaskCriteria = new WorkflowTaskCriteria();
        var copy = workflowTaskCriteria.copy();

        assertThat(workflowTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowTaskCriteria)
        );
    }

    @Test
    void workflowTaskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workflowTaskCriteria = new WorkflowTaskCriteria();
        setAllFilters(workflowTaskCriteria);

        var copy = workflowTaskCriteria.copy();

        assertThat(workflowTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowTaskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workflowTaskCriteria = new WorkflowTaskCriteria();

        assertThat(workflowTaskCriteria).hasToString("WorkflowTaskCriteria{}");
    }

    private static void setAllFilters(WorkflowTaskCriteria workflowTaskCriteria) {
        workflowTaskCriteria.id();
        workflowTaskCriteria.assigneeId();
        workflowTaskCriteria.status();
        workflowTaskCriteria.action();
        workflowTaskCriteria.assignedDate();
        workflowTaskCriteria.dueDate();
        workflowTaskCriteria.completedDate();
        workflowTaskCriteria.reminderSent();
        workflowTaskCriteria.delegatedTo();
        workflowTaskCriteria.delegatedDate();
        workflowTaskCriteria.instanceId();
        workflowTaskCriteria.stepId();
        workflowTaskCriteria.distinct();
    }

    private static Condition<WorkflowTaskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssigneeId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getAssignedDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getCompletedDate()) &&
                condition.apply(criteria.getReminderSent()) &&
                condition.apply(criteria.getDelegatedTo()) &&
                condition.apply(criteria.getDelegatedDate()) &&
                condition.apply(criteria.getInstanceId()) &&
                condition.apply(criteria.getStepId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkflowTaskCriteria> copyFiltersAre(
        WorkflowTaskCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssigneeId(), copy.getAssigneeId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getAssignedDate(), copy.getAssignedDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getCompletedDate(), copy.getCompletedDate()) &&
                condition.apply(criteria.getReminderSent(), copy.getReminderSent()) &&
                condition.apply(criteria.getDelegatedTo(), copy.getDelegatedTo()) &&
                condition.apply(criteria.getDelegatedDate(), copy.getDelegatedDate()) &&
                condition.apply(criteria.getInstanceId(), copy.getInstanceId()) &&
                condition.apply(criteria.getStepId(), copy.getStepId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
