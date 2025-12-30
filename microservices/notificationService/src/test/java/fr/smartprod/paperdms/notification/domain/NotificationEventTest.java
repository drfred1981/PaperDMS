package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.NotificationEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationEvent.class);
        NotificationEvent notificationEvent1 = getNotificationEventSample1();
        NotificationEvent notificationEvent2 = new NotificationEvent();
        assertThat(notificationEvent1).isNotEqualTo(notificationEvent2);

        notificationEvent2.setId(notificationEvent1.getId());
        assertThat(notificationEvent1).isEqualTo(notificationEvent2);

        notificationEvent2 = getNotificationEventSample2();
        assertThat(notificationEvent1).isNotEqualTo(notificationEvent2);
    }
}
