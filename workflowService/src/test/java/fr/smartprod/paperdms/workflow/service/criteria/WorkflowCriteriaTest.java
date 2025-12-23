package fr.smartprod.paperdms.workflow.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WorkflowCriteriaTest {

    @Test
    void newWorkflowCriteriaHasAllFiltersNullTest() {
        var workflowCriteria = new WorkflowCriteria();
        assertThat(workflowCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void workflowCriteriaFluentMethodsCreatesFiltersTest() {
        var workflowCriteria = new WorkflowCriteria();

        setAllFilters(workflowCriteria);

        assertThat(workflowCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void workflowCriteriaCopyCreatesNullFilterTest() {
        var workflowCriteria = new WorkflowCriteria();
        var copy = workflowCriteria.copy();

        assertThat(workflowCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowCriteria)
        );
    }

    @Test
    void workflowCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var workflowCriteria = new WorkflowCriteria();
        setAllFilters(workflowCriteria);

        var copy = workflowCriteria.copy();

        assertThat(workflowCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(workflowCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var workflowCriteria = new WorkflowCriteria();

        assertThat(workflowCriteria).hasToString("WorkflowCriteria{}");
    }

    private static void setAllFilters(WorkflowCriteria workflowCriteria) {
        workflowCriteria.id();
        workflowCriteria.name();
        workflowCriteria.version();
        workflowCriteria.isActive();
        workflowCriteria.isParallel();
        workflowCriteria.autoStart();
        workflowCriteria.triggerEvent();
        workflowCriteria.createdDate();
        workflowCriteria.lastModifiedDate();
        workflowCriteria.createdBy();
        workflowCriteria.distinct();
    }

    private static Condition<WorkflowCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getIsParallel()) &&
                condition.apply(criteria.getAutoStart()) &&
                condition.apply(criteria.getTriggerEvent()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WorkflowCriteria> copyFiltersAre(WorkflowCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getIsParallel(), copy.getIsParallel()) &&
                condition.apply(criteria.getAutoStart(), copy.getAutoStart()) &&
                condition.apply(criteria.getTriggerEvent(), copy.getTriggerEvent()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
