package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookLogTestSamples.*;
import static fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationWebhookLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationWebhookLog.class);
        NotificationWebhookLog notificationWebhookLog1 = getNotificationWebhookLogSample1();
        NotificationWebhookLog notificationWebhookLog2 = new NotificationWebhookLog();
        assertThat(notificationWebhookLog1).isNotEqualTo(notificationWebhookLog2);

        notificationWebhookLog2.setId(notificationWebhookLog1.getId());
        assertThat(notificationWebhookLog1).isEqualTo(notificationWebhookLog2);

        notificationWebhookLog2 = getNotificationWebhookLogSample2();
        assertThat(notificationWebhookLog1).isNotEqualTo(notificationWebhookLog2);
    }

    @Test
    void subscriptionTest() {
        NotificationWebhookLog notificationWebhookLog = getNotificationWebhookLogRandomSampleGenerator();
        NotificationWebhookSubscription notificationWebhookSubscriptionBack = getNotificationWebhookSubscriptionRandomSampleGenerator();

        notificationWebhookLog.setSubscription(notificationWebhookSubscriptionBack);
        assertThat(notificationWebhookLog.getSubscription()).isEqualTo(notificationWebhookSubscriptionBack);

        notificationWebhookLog.subscription(null);
        assertThat(notificationWebhookLog.getSubscription()).isNull();
    }
}
