package fr.smartprod.paperdms.notification.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationWebhookSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationWebhookSubscriptionDTO.class);
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO1 = new NotificationWebhookSubscriptionDTO();
        notificationWebhookSubscriptionDTO1.setId(1L);
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO2 = new NotificationWebhookSubscriptionDTO();
        assertThat(notificationWebhookSubscriptionDTO1).isNotEqualTo(notificationWebhookSubscriptionDTO2);
        notificationWebhookSubscriptionDTO2.setId(notificationWebhookSubscriptionDTO1.getId());
        assertThat(notificationWebhookSubscriptionDTO1).isEqualTo(notificationWebhookSubscriptionDTO2);
        notificationWebhookSubscriptionDTO2.setId(2L);
        assertThat(notificationWebhookSubscriptionDTO1).isNotEqualTo(notificationWebhookSubscriptionDTO2);
        notificationWebhookSubscriptionDTO1.setId(null);
        assertThat(notificationWebhookSubscriptionDTO1).isNotEqualTo(notificationWebhookSubscriptionDTO2);
    }
}
