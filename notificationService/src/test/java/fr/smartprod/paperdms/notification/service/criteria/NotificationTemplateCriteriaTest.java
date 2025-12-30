package fr.smartprod.paperdms.notification.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationTemplateCriteriaTest {

    @Test
    void newNotificationTemplateCriteriaHasAllFiltersNullTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        assertThat(notificationTemplateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();

        setAllFilters(notificationTemplateCriteria);

        assertThat(notificationTemplateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationTemplateCriteriaCopyCreatesNullFilterTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        var copy = notificationTemplateCriteria.copy();

        assertThat(notificationTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationTemplateCriteria)
        );
    }

    @Test
    void notificationTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        setAllFilters(notificationTemplateCriteria);

        var copy = notificationTemplateCriteria.copy();

        assertThat(notificationTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();

        assertThat(notificationTemplateCriteria).hasToString("NotificationTemplateCriteria{}");
    }

    private static void setAllFilters(NotificationTemplateCriteria notificationTemplateCriteria) {
        notificationTemplateCriteria.id();
        notificationTemplateCriteria.name();
        notificationTemplateCriteria.subject();
        notificationTemplateCriteria.type();
        notificationTemplateCriteria.channel();
        notificationTemplateCriteria.isActive();
        notificationTemplateCriteria.createdDate();
        notificationTemplateCriteria.lastModifiedDate();
        notificationTemplateCriteria.notificationsId();
        notificationTemplateCriteria.distinct();
    }

    private static Condition<NotificationTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getChannel()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getNotificationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationTemplateCriteria> copyFiltersAre(
        NotificationTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getChannel(), copy.getChannel()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getNotificationsId(), copy.getNotificationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
