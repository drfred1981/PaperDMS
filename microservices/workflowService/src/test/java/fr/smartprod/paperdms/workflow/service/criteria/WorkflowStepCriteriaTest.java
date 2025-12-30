package fr.smartprod.paperdms.workflow.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkflowStepCriteriaTest {

    @Test
    void newWorkflowStepCriteriaHasAllFiltersNullTest() {
        var workflowStepCriteria = new WorkflowStepCriteria();
        assertThat(workflowStepCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void workflowStepCriteriaFluentMethodsCreatesFiltersTest() {
        var workflowStepCriteria = new WorkflowStepCriteria();

        setAllFilters(workflowStepCriteria);

        assertThat(workflowStepCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void workflowStepCriteriaCopyCreatesNullFilterTest() {
        var workflowStepCriteria = new WorkflowStepCriteria();
        var copy = workflowStepCriteria.copy();

        assertThat(workflowStepCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowStepCriteria)
        );
    }

    @Test
    void workflowStepCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workflowStepCriteria = new WorkflowStepCriteria();
        setAllFilters(workflowStepCriteria);

        var copy = workflowStepCriteria.copy();

        assertThat(workflowStepCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowStepCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workflowStepCriteria = new WorkflowStepCriteria();

        assertThat(workflowStepCriteria).hasToString("WorkflowStepCriteria{}");
    }

    private static void setAllFilters(WorkflowStepCriteria workflowStepCriteria) {
        workflowStepCriteria.id();
        workflowStepCriteria.stepNumber();
        workflowStepCriteria.name();
        workflowStepCriteria.stepType();
        workflowStepCriteria.assigneeType();
        workflowStepCriteria.assigneeId();
        workflowStepCriteria.assigneeGroup();
        workflowStepCriteria.dueInDays();
        workflowStepCriteria.isRequired();
        workflowStepCriteria.canDelegate();
        workflowStepCriteria.canReject();
        workflowStepCriteria.workflowTasksId();
        workflowStepCriteria.workflowId();
        workflowStepCriteria.distinct();
    }

    private static Condition<WorkflowStepCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStepNumber()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStepType()) &&
                condition.apply(criteria.getAssigneeType()) &&
                condition.apply(criteria.getAssigneeId()) &&
                condition.apply(criteria.getAssigneeGroup()) &&
                condition.apply(criteria.getDueInDays()) &&
                condition.apply(criteria.getIsRequired()) &&
                condition.apply(criteria.getCanDelegate()) &&
                condition.apply(criteria.getCanReject()) &&
                condition.apply(criteria.getWorkflowTasksId()) &&
                condition.apply(criteria.getWorkflowId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkflowStepCriteria> copyFiltersAre(
        WorkflowStepCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStepNumber(), copy.getStepNumber()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStepType(), copy.getStepType()) &&
                condition.apply(criteria.getAssigneeType(), copy.getAssigneeType()) &&
                condition.apply(criteria.getAssigneeId(), copy.getAssigneeId()) &&
                condition.apply(criteria.getAssigneeGroup(), copy.getAssigneeGroup()) &&
                condition.apply(criteria.getDueInDays(), copy.getDueInDays()) &&
                condition.apply(criteria.getIsRequired(), copy.getIsRequired()) &&
                condition.apply(criteria.getCanDelegate(), copy.getCanDelegate()) &&
                condition.apply(criteria.getCanReject(), copy.getCanReject()) &&
                condition.apply(criteria.getWorkflowTasksId(), copy.getWorkflowTasksId()) &&
                condition.apply(criteria.getWorkflowId(), copy.getWorkflowId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
