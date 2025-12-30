package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationEventCriteriaTest {

    @Test
    void newNotificationEventCriteriaHasAllFiltersNullTest() {
        var notificationEventCriteria = new NotificationEventCriteria();
        assertThat(notificationEventCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationEventCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationEventCriteria = new NotificationEventCriteria();

        setAllFilters(notificationEventCriteria);

        assertThat(notificationEventCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationEventCriteriaCopyCreatesNullFilterTest() {
        var notificationEventCriteria = new NotificationEventCriteria();
        var copy = notificationEventCriteria.copy();

        assertThat(notificationEventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationEventCriteria)
        );
    }

    @Test
    void notificationEventCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationEventCriteria = new NotificationEventCriteria();
        setAllFilters(notificationEventCriteria);

        var copy = notificationEventCriteria.copy();

        assertThat(notificationEventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationEventCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationEventCriteria = new NotificationEventCriteria();

        assertThat(notificationEventCriteria).hasToString("NotificationEventCriteria{}");
    }

    private static void setAllFilters(NotificationEventCriteria notificationEventCriteria) {
        notificationEventCriteria.id();
        notificationEventCriteria.eventType();
        notificationEventCriteria.entityType();
        notificationEventCriteria.entityName();
        notificationEventCriteria.userId();
        notificationEventCriteria.eventDate();
        notificationEventCriteria.processed();
        notificationEventCriteria.processedDate();
        notificationEventCriteria.distinct();
    }

    private static Condition<NotificationEventCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getEntityType()) &&
                condition.apply(criteria.getEntityName()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getEventDate()) &&
                condition.apply(criteria.getProcessed()) &&
                condition.apply(criteria.getProcessedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationEventCriteria> copyFiltersAre(
        NotificationEventCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getEntityType(), copy.getEntityType()) &&
                condition.apply(criteria.getEntityName(), copy.getEntityName()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getEventDate(), copy.getEventDate()) &&
                condition.apply(criteria.getProcessed(), copy.getProcessed()) &&
                condition.apply(criteria.getProcessedDate(), copy.getProcessedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
