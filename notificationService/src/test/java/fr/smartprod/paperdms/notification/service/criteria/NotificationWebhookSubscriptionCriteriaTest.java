package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationWebhookSubscriptionCriteriaTest {

    @Test
    void newNotificationWebhookSubscriptionCriteriaHasAllFiltersNullTest() {
        var notificationWebhookSubscriptionCriteria = new NotificationWebhookSubscriptionCriteria();
        assertThat(notificationWebhookSubscriptionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationWebhookSubscriptionCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationWebhookSubscriptionCriteria = new NotificationWebhookSubscriptionCriteria();

        setAllFilters(notificationWebhookSubscriptionCriteria);

        assertThat(notificationWebhookSubscriptionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationWebhookSubscriptionCriteriaCopyCreatesNullFilterTest() {
        var notificationWebhookSubscriptionCriteria = new NotificationWebhookSubscriptionCriteria();
        var copy = notificationWebhookSubscriptionCriteria.copy();

        assertThat(notificationWebhookSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationWebhookSubscriptionCriteria)
        );
    }

    @Test
    void notificationWebhookSubscriptionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationWebhookSubscriptionCriteria = new NotificationWebhookSubscriptionCriteria();
        setAllFilters(notificationWebhookSubscriptionCriteria);

        var copy = notificationWebhookSubscriptionCriteria.copy();

        assertThat(notificationWebhookSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationWebhookSubscriptionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationWebhookSubscriptionCriteria = new NotificationWebhookSubscriptionCriteria();

        assertThat(notificationWebhookSubscriptionCriteria).hasToString("NotificationWebhookSubscriptionCriteria{}");
    }

    private static void setAllFilters(NotificationWebhookSubscriptionCriteria notificationWebhookSubscriptionCriteria) {
        notificationWebhookSubscriptionCriteria.id();
        notificationWebhookSubscriptionCriteria.name();
        notificationWebhookSubscriptionCriteria.url();
        notificationWebhookSubscriptionCriteria.secret();
        notificationWebhookSubscriptionCriteria.isActive();
        notificationWebhookSubscriptionCriteria.retryCount();
        notificationWebhookSubscriptionCriteria.maxRetries();
        notificationWebhookSubscriptionCriteria.retryDelay();
        notificationWebhookSubscriptionCriteria.lastTriggerDate();
        notificationWebhookSubscriptionCriteria.lastSuccessDate();
        notificationWebhookSubscriptionCriteria.failureCount();
        notificationWebhookSubscriptionCriteria.createdBy();
        notificationWebhookSubscriptionCriteria.createdDate();
        notificationWebhookSubscriptionCriteria.lastModifiedDate();
        notificationWebhookSubscriptionCriteria.notificationWebhookLogsId();
        notificationWebhookSubscriptionCriteria.distinct();
    }

    private static Condition<NotificationWebhookSubscriptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getSecret()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getRetryCount()) &&
                condition.apply(criteria.getMaxRetries()) &&
                condition.apply(criteria.getRetryDelay()) &&
                condition.apply(criteria.getLastTriggerDate()) &&
                condition.apply(criteria.getLastSuccessDate()) &&
                condition.apply(criteria.getFailureCount()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getNotificationWebhookLogsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationWebhookSubscriptionCriteria> copyFiltersAre(
        NotificationWebhookSubscriptionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getSecret(), copy.getSecret()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getRetryCount(), copy.getRetryCount()) &&
                condition.apply(criteria.getMaxRetries(), copy.getMaxRetries()) &&
                condition.apply(criteria.getRetryDelay(), copy.getRetryDelay()) &&
                condition.apply(criteria.getLastTriggerDate(), copy.getLastTriggerDate()) &&
                condition.apply(criteria.getLastSuccessDate(), copy.getLastSuccessDate()) &&
                condition.apply(criteria.getFailureCount(), copy.getFailureCount()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getNotificationWebhookLogsId(), copy.getNotificationWebhookLogsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
