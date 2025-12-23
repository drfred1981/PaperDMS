package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WebhookSubscriptionCriteriaTest {

    @Test
    void newWebhookSubscriptionCriteriaHasAllFiltersNullTest() {
        var webhookSubscriptionCriteria = new WebhookSubscriptionCriteria();
        assertThat(webhookSubscriptionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void webhookSubscriptionCriteriaFluentMethodsCreatesFiltersTest() {
        var webhookSubscriptionCriteria = new WebhookSubscriptionCriteria();

        setAllFilters(webhookSubscriptionCriteria);

        assertThat(webhookSubscriptionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void webhookSubscriptionCriteriaCopyCreatesNullFilterTest() {
        var webhookSubscriptionCriteria = new WebhookSubscriptionCriteria();
        var copy = webhookSubscriptionCriteria.copy();

        assertThat(webhookSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(webhookSubscriptionCriteria)
        );
    }

    @Test
    void webhookSubscriptionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var webhookSubscriptionCriteria = new WebhookSubscriptionCriteria();
        setAllFilters(webhookSubscriptionCriteria);

        var copy = webhookSubscriptionCriteria.copy();

        assertThat(webhookSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(webhookSubscriptionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var webhookSubscriptionCriteria = new WebhookSubscriptionCriteria();

        assertThat(webhookSubscriptionCriteria).hasToString("WebhookSubscriptionCriteria{}");
    }

    private static void setAllFilters(WebhookSubscriptionCriteria webhookSubscriptionCriteria) {
        webhookSubscriptionCriteria.id();
        webhookSubscriptionCriteria.name();
        webhookSubscriptionCriteria.url();
        webhookSubscriptionCriteria.secret();
        webhookSubscriptionCriteria.isActive();
        webhookSubscriptionCriteria.retryCount();
        webhookSubscriptionCriteria.maxRetries();
        webhookSubscriptionCriteria.retryDelay();
        webhookSubscriptionCriteria.lastTriggerDate();
        webhookSubscriptionCriteria.lastSuccessDate();
        webhookSubscriptionCriteria.failureCount();
        webhookSubscriptionCriteria.createdBy();
        webhookSubscriptionCriteria.createdDate();
        webhookSubscriptionCriteria.lastModifiedDate();
        webhookSubscriptionCriteria.distinct();
    }

    private static Condition<WebhookSubscriptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WebhookSubscriptionCriteria> copyFiltersAre(
        WebhookSubscriptionCriteria copy,
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
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
