package fr.smartprod.paperdms.notification.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationEventDTO.class);
        NotificationEventDTO notificationEventDTO1 = new NotificationEventDTO();
        notificationEventDTO1.setId(1L);
        NotificationEventDTO notificationEventDTO2 = new NotificationEventDTO();
        assertThat(notificationEventDTO1).isNotEqualTo(notificationEventDTO2);
        notificationEventDTO2.setId(notificationEventDTO1.getId());
        assertThat(notificationEventDTO1).isEqualTo(notificationEventDTO2);
        notificationEventDTO2.setId(2L);
        assertThat(notificationEventDTO1).isNotEqualTo(notificationEventDTO2);
        notificationEventDTO1.setId(null);
        assertThat(notificationEventDTO1).isNotEqualTo(notificationEventDTO2);
    }
}
