package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlertCriteriaTest {

    @Test
    void newAlertCriteriaHasAllFiltersNullTest() {
        var alertCriteria = new AlertCriteria();
        assertThat(alertCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alertCriteriaFluentMethodsCreatesFiltersTest() {
        var alertCriteria = new AlertCriteria();

        setAllFilters(alertCriteria);

        assertThat(alertCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alertCriteriaCopyCreatesNullFilterTest() {
        var alertCriteria = new AlertCriteria();
        var copy = alertCriteria.copy();

        assertThat(alertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alertCriteria)
        );
    }

    @Test
    void alertCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alertCriteria = new AlertCriteria();
        setAllFilters(alertCriteria);

        var copy = alertCriteria.copy();

        assertThat(alertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alertCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alertCriteria = new AlertCriteria();

        assertThat(alertCriteria).hasToString("AlertCriteria{}");
    }

    private static void setAllFilters(AlertCriteria alertCriteria) {
        alertCriteria.id();
        alertCriteria.alertRuleId();
        alertCriteria.severity();
        alertCriteria.title();
        alertCriteria.entityType();
        alertCriteria.entityId();
        alertCriteria.status();
        alertCriteria.triggeredDate();
        alertCriteria.acknowledgedBy();
        alertCriteria.acknowledgedDate();
        alertCriteria.resolvedBy();
        alertCriteria.resolvedDate();
        alertCriteria.alertRuleId();
        alertCriteria.distinct();
    }

    private static Condition<AlertCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAlertRuleId()) &&
                condition.apply(criteria.getSeverity()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getEntityType()) &&
                condition.apply(criteria.getEntityId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTriggeredDate()) &&
                condition.apply(criteria.getAcknowledgedBy()) &&
                condition.apply(criteria.getAcknowledgedDate()) &&
                condition.apply(criteria.getResolvedBy()) &&
                condition.apply(criteria.getResolvedDate()) &&
                condition.apply(criteria.getAlertRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlertCriteria> copyFiltersAre(AlertCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAlertRuleId(), copy.getAlertRuleId()) &&
                condition.apply(criteria.getSeverity(), copy.getSeverity()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getEntityType(), copy.getEntityType()) &&
                condition.apply(criteria.getEntityId(), copy.getEntityId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTriggeredDate(), copy.getTriggeredDate()) &&
                condition.apply(criteria.getAcknowledgedBy(), copy.getAcknowledgedBy()) &&
                condition.apply(criteria.getAcknowledgedDate(), copy.getAcknowledgedDate()) &&
                condition.apply(criteria.getResolvedBy(), copy.getResolvedBy()) &&
                condition.apply(criteria.getResolvedDate(), copy.getResolvedDate()) &&
                condition.apply(criteria.getAlertRuleId(), copy.getAlertRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
