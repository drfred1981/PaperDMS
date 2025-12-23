package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlertRuleCriteriaTest {

    @Test
    void newAlertRuleCriteriaHasAllFiltersNullTest() {
        var alertRuleCriteria = new AlertRuleCriteria();
        assertThat(alertRuleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alertRuleCriteriaFluentMethodsCreatesFiltersTest() {
        var alertRuleCriteria = new AlertRuleCriteria();

        setAllFilters(alertRuleCriteria);

        assertThat(alertRuleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alertRuleCriteriaCopyCreatesNullFilterTest() {
        var alertRuleCriteria = new AlertRuleCriteria();
        var copy = alertRuleCriteria.copy();

        assertThat(alertRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alertRuleCriteria)
        );
    }

    @Test
    void alertRuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alertRuleCriteria = new AlertRuleCriteria();
        setAllFilters(alertRuleCriteria);

        var copy = alertRuleCriteria.copy();

        assertThat(alertRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alertRuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alertRuleCriteria = new AlertRuleCriteria();

        assertThat(alertRuleCriteria).hasToString("AlertRuleCriteria{}");
    }

    private static void setAllFilters(AlertRuleCriteria alertRuleCriteria) {
        alertRuleCriteria.id();
        alertRuleCriteria.name();
        alertRuleCriteria.alertType();
        alertRuleCriteria.severity();
        alertRuleCriteria.isActive();
        alertRuleCriteria.triggerCount();
        alertRuleCriteria.lastTriggered();
        alertRuleCriteria.createdBy();
        alertRuleCriteria.createdDate();
        alertRuleCriteria.distinct();
    }

    private static Condition<AlertRuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAlertType()) &&
                condition.apply(criteria.getSeverity()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTriggerCount()) &&
                condition.apply(criteria.getLastTriggered()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlertRuleCriteria> copyFiltersAre(AlertRuleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAlertType(), copy.getAlertType()) &&
                condition.apply(criteria.getSeverity(), copy.getSeverity()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTriggerCount(), copy.getTriggerCount()) &&
                condition.apply(criteria.getLastTriggered(), copy.getLastTriggered()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
