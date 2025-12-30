package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookLogTestSamples.*;
import static fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NotificationWebhookSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationWebhookSubscription.class);
        NotificationWebhookSubscription notificationWebhookSubscription1 = getNotificationWebhookSubscriptionSample1();
        NotificationWebhookSubscription notificationWebhookSubscription2 = new NotificationWebhookSubscription();
        assertThat(notificationWebhookSubscription1).isNotEqualTo(notificationWebhookSubscription2);

        notificationWebhookSubscription2.setId(notificationWebhookSubscription1.getId());
        assertThat(notificationWebhookSubscription1).isEqualTo(notificationWebhookSubscription2);

        notificationWebhookSubscription2 = getNotificationWebhookSubscriptionSample2();
        assertThat(notificationWebhookSubscription1).isNotEqualTo(notificationWebhookSubscription2);
    }

    @Test
    void notificationWebhookLogsTest() {
        NotificationWebhookSubscription notificationWebhookSubscription = getNotificationWebhookSubscriptionRandomSampleGenerator();
        NotificationWebhookLog notificationWebhookLogBack = getNotificationWebhookLogRandomSampleGenerator();

        notificationWebhookSubscription.addNotificationWebhookLogs(notificationWebhookLogBack);
        assertThat(notificationWebhookSubscription.getNotificationWebhookLogs()).containsOnly(notificationWebhookLogBack);
        assertThat(notificationWebhookLogBack.getSubscription()).isEqualTo(notificationWebhookSubscription);

        notificationWebhookSubscription.removeNotificationWebhookLogs(notificationWebhookLogBack);
        assertThat(notificationWebhookSubscription.getNotificationWebhookLogs()).doesNotContain(notificationWebhookLogBack);
        assertThat(notificationWebhookLogBack.getSubscription()).isNull();

        notificationWebhookSubscription.notificationWebhookLogs(new HashSet<>(Set.of(notificationWebhookLogBack)));
        assertThat(notificationWebhookSubscription.getNotificationWebhookLogs()).containsOnly(notificationWebhookLogBack);
        assertThat(notificationWebhookLogBack.getSubscription()).isEqualTo(notificationWebhookSubscription);

        notificationWebhookSubscription.setNotificationWebhookLogs(new HashSet<>());
        assertThat(notificationWebhookSubscription.getNotificationWebhookLogs()).doesNotContain(notificationWebhookLogBack);
        assertThat(notificationWebhookLogBack.getSubscription()).isNull();
    }
}
