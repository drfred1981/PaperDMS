package fr.smartprod.paperdms.notification.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebhookLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebhookLogDTO.class);
        WebhookLogDTO webhookLogDTO1 = new WebhookLogDTO();
        webhookLogDTO1.setId(1L);
        WebhookLogDTO webhookLogDTO2 = new WebhookLogDTO();
        assertThat(webhookLogDTO1).isNotEqualTo(webhookLogDTO2);
        webhookLogDTO2.setId(webhookLogDTO1.getId());
        assertThat(webhookLogDTO1).isEqualTo(webhookLogDTO2);
        webhookLogDTO2.setId(2L);
        assertThat(webhookLogDTO1).isNotEqualTo(webhookLogDTO2);
        webhookLogDTO1.setId(null);
        assertThat(webhookLogDTO1).isNotEqualTo(webhookLogDTO2);
    }
}
