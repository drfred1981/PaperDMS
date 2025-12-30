package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationWebhookLogCriteriaTest {

    @Test
    void newNotificationWebhookLogCriteriaHasAllFiltersNullTest() {
        var notificationWebhookLogCriteria = new NotificationWebhookLogCriteria();
        assertThat(notificationWebhookLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationWebhookLogCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationWebhookLogCriteria = new NotificationWebhookLogCriteria();

        setAllFilters(notificationWebhookLogCriteria);

        assertThat(notificationWebhookLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationWebhookLogCriteriaCopyCreatesNullFilterTest() {
        var notificationWebhookLogCriteria = new NotificationWebhookLogCriteria();
        var copy = notificationWebhookLogCriteria.copy();

        assertThat(notificationWebhookLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationWebhookLogCriteria)
        );
    }

    @Test
    void notificationWebhookLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationWebhookLogCriteria = new NotificationWebhookLogCriteria();
        setAllFilters(notificationWebhookLogCriteria);

        var copy = notificationWebhookLogCriteria.copy();

        assertThat(notificationWebhookLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationWebhookLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationWebhookLogCriteria = new NotificationWebhookLogCriteria();

        assertThat(notificationWebhookLogCriteria).hasToString("NotificationWebhookLogCriteria{}");
    }

    private static void setAllFilters(NotificationWebhookLogCriteria notificationWebhookLogCriteria) {
        notificationWebhookLogCriteria.id();
        notificationWebhookLogCriteria.eventType();
        notificationWebhookLogCriteria.responseStatus();
        notificationWebhookLogCriteria.responseTime();
        notificationWebhookLogCriteria.attemptNumber();
        notificationWebhookLogCriteria.isSuccess();
        notificationWebhookLogCriteria.sentDate();
        notificationWebhookLogCriteria.subscriptionId();
        notificationWebhookLogCriteria.distinct();
    }

    private static Condition<NotificationWebhookLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getResponseStatus()) &&
                condition.apply(criteria.getResponseTime()) &&
                condition.apply(criteria.getAttemptNumber()) &&
                condition.apply(criteria.getIsSuccess()) &&
                condition.apply(criteria.getSentDate()) &&
                condition.apply(criteria.getSubscriptionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationWebhookLogCriteria> copyFiltersAre(
        NotificationWebhookLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getResponseStatus(), copy.getResponseStatus()) &&
                condition.apply(criteria.getResponseTime(), copy.getResponseTime()) &&
                condition.apply(criteria.getAttemptNumber(), copy.getAttemptNumber()) &&
                condition.apply(criteria.getIsSuccess(), copy.getIsSuccess()) &&
                condition.apply(criteria.getSentDate(), copy.getSentDate()) &&
                condition.apply(criteria.getSubscriptionId(), copy.getSubscriptionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
