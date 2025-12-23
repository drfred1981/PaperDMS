package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.WebhookLogTestSamples.*;
import static fr.smartprod.paperdms.notification.domain.WebhookSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebhookLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebhookLog.class);
        WebhookLog webhookLog1 = getWebhookLogSample1();
        WebhookLog webhookLog2 = new WebhookLog();
        assertThat(webhookLog1).isNotEqualTo(webhookLog2);

        webhookLog2.setId(webhookLog1.getId());
        assertThat(webhookLog1).isEqualTo(webhookLog2);

        webhookLog2 = getWebhookLogSample2();
        assertThat(webhookLog1).isNotEqualTo(webhookLog2);
    }

    @Test
    void subscriptionTest() {
        WebhookLog webhookLog = getWebhookLogRandomSampleGenerator();
        WebhookSubscription webhookSubscriptionBack = getWebhookSubscriptionRandomSampleGenerator();

        webhookLog.setSubscription(webhookSubscriptionBack);
        assertThat(webhookLog.getSubscription()).isEqualTo(webhookSubscriptionBack);

        webhookLog.subscription(null);
        assertThat(webhookLog.getSubscription()).isNull();
    }
}
