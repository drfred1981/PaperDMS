package fr.smartprod.paperdms.workflow.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkflowApprovalHistoryCriteriaTest {

    @Test
    void newWorkflowApprovalHistoryCriteriaHasAllFiltersNullTest() {
        var workflowApprovalHistoryCriteria = new WorkflowApprovalHistoryCriteria();
        assertThat(workflowApprovalHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void workflowApprovalHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var workflowApprovalHistoryCriteria = new WorkflowApprovalHistoryCriteria();

        setAllFilters(workflowApprovalHistoryCriteria);

        assertThat(workflowApprovalHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void workflowApprovalHistoryCriteriaCopyCreatesNullFilterTest() {
        var workflowApprovalHistoryCriteria = new WorkflowApprovalHistoryCriteria();
        var copy = workflowApprovalHistoryCriteria.copy();

        assertThat(workflowApprovalHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowApprovalHistoryCriteria)
        );
    }

    @Test
    void workflowApprovalHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workflowApprovalHistoryCriteria = new WorkflowApprovalHistoryCriteria();
        setAllFilters(workflowApprovalHistoryCriteria);

        var copy = workflowApprovalHistoryCriteria.copy();

        assertThat(workflowApprovalHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowApprovalHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workflowApprovalHistoryCriteria = new WorkflowApprovalHistoryCriteria();

        assertThat(workflowApprovalHistoryCriteria).hasToString("WorkflowApprovalHistoryCriteria{}");
    }

    private static void setAllFilters(WorkflowApprovalHistoryCriteria workflowApprovalHistoryCriteria) {
        workflowApprovalHistoryCriteria.id();
        workflowApprovalHistoryCriteria.documentSha256();
        workflowApprovalHistoryCriteria.stepNumber();
        workflowApprovalHistoryCriteria.action();
        workflowApprovalHistoryCriteria.actionDate();
        workflowApprovalHistoryCriteria.actionBy();
        workflowApprovalHistoryCriteria.previousAssignee();
        workflowApprovalHistoryCriteria.timeTaken();
        workflowApprovalHistoryCriteria.workflowInstanceId();
        workflowApprovalHistoryCriteria.distinct();
    }

    private static Condition<WorkflowApprovalHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getStepNumber()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getActionDate()) &&
                condition.apply(criteria.getActionBy()) &&
                condition.apply(criteria.getPreviousAssignee()) &&
                condition.apply(criteria.getTimeTaken()) &&
                condition.apply(criteria.getWorkflowInstanceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkflowApprovalHistoryCriteria> copyFiltersAre(
        WorkflowApprovalHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getStepNumber(), copy.getStepNumber()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getActionDate(), copy.getActionDate()) &&
                condition.apply(criteria.getActionBy(), copy.getActionBy()) &&
                condition.apply(criteria.getPreviousAssignee(), copy.getPreviousAssignee()) &&
                condition.apply(criteria.getTimeTaken(), copy.getTimeTaken()) &&
                condition.apply(criteria.getWorkflowInstanceId(), copy.getWorkflowInstanceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
