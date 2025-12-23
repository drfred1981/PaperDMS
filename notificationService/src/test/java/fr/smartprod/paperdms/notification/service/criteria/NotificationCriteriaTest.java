package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationCriteriaTest {

    @Test
    void newNotificationCriteriaHasAllFiltersNullTest() {
        var notificationCriteria = new NotificationCriteria();
        assertThat(notificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationCriteria = new NotificationCriteria();

        setAllFilters(notificationCriteria);

        assertThat(notificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationCriteriaCopyCreatesNullFilterTest() {
        var notificationCriteria = new NotificationCriteria();
        var copy = notificationCriteria.copy();

        assertThat(notificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationCriteria)
        );
    }

    @Test
    void notificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationCriteria = new NotificationCriteria();
        setAllFilters(notificationCriteria);

        var copy = notificationCriteria.copy();

        assertThat(notificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationCriteria = new NotificationCriteria();

        assertThat(notificationCriteria).hasToString("NotificationCriteria{}");
    }

    private static void setAllFilters(NotificationCriteria notificationCriteria) {
        notificationCriteria.id();
        notificationCriteria.title();
        notificationCriteria.type();
        notificationCriteria.priority();
        notificationCriteria.recipientId();
        notificationCriteria.isRead();
        notificationCriteria.readDate();
        notificationCriteria.channel();
        notificationCriteria.relatedEntityType();
        notificationCriteria.relatedEntityId();
        notificationCriteria.actionUrl();
        notificationCriteria.expirationDate();
        notificationCriteria.sentDate();
        notificationCriteria.createdDate();
        notificationCriteria.templateId();
        notificationCriteria.distinct();
    }

    private static Condition<NotificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getRecipientId()) &&
                condition.apply(criteria.getIsRead()) &&
                condition.apply(criteria.getReadDate()) &&
                condition.apply(criteria.getChannel()) &&
                condition.apply(criteria.getRelatedEntityType()) &&
                condition.apply(criteria.getRelatedEntityId()) &&
                condition.apply(criteria.getActionUrl()) &&
                condition.apply(criteria.getExpirationDate()) &&
                condition.apply(criteria.getSentDate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getTemplateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationCriteria> copyFiltersAre(
        NotificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getRecipientId(), copy.getRecipientId()) &&
                condition.apply(criteria.getIsRead(), copy.getIsRead()) &&
                condition.apply(criteria.getReadDate(), copy.getReadDate()) &&
                condition.apply(criteria.getChannel(), copy.getChannel()) &&
                condition.apply(criteria.getRelatedEntityType(), copy.getRelatedEntityType()) &&
                condition.apply(criteria.getRelatedEntityId(), copy.getRelatedEntityId()) &&
                condition.apply(criteria.getActionUrl(), copy.getActionUrl()) &&
                condition.apply(criteria.getExpirationDate(), copy.getExpirationDate()) &&
                condition.apply(criteria.getSentDate(), copy.getSentDate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getTemplateId(), copy.getTemplateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
