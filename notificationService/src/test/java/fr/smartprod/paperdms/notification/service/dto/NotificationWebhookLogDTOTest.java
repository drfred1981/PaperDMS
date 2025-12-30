package fr.smartprod.paperdms.notification.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationWebhookLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationWebhookLogDTO.class);
        NotificationWebhookLogDTO notificationWebhookLogDTO1 = new NotificationWebhookLogDTO();
        notificationWebhookLogDTO1.setId(1L);
        NotificationWebhookLogDTO notificationWebhookLogDTO2 = new NotificationWebhookLogDTO();
        assertThat(notificationWebhookLogDTO1).isNotEqualTo(notificationWebhookLogDTO2);
        notificationWebhookLogDTO2.setId(notificationWebhookLogDTO1.getId());
        assertThat(notificationWebhookLogDTO1).isEqualTo(notificationWebhookLogDTO2);
        notificationWebhookLogDTO2.setId(2L);
        assertThat(notificationWebhookLogDTO1).isNotEqualTo(notificationWebhookLogDTO2);
        notificationWebhookLogDTO1.setId(null);
        assertThat(notificationWebhookLogDTO1).isNotEqualTo(notificationWebhookLogDTO2);
    }
}
