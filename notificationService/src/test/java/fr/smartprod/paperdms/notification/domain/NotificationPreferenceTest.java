package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.NotificationPreferenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationPreferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationPreference.class);
        NotificationPreference notificationPreference1 = getNotificationPreferenceSample1();
        NotificationPreference notificationPreference2 = new NotificationPreference();
        assertThat(notificationPreference1).isNotEqualTo(notificationPreference2);

        notificationPreference2.setId(notificationPreference1.getId());
        assertThat(notificationPreference1).isEqualTo(notificationPreference2);

        notificationPreference2 = getNotificationPreferenceSample2();
        assertThat(notificationPreference1).isNotEqualTo(notificationPreference2);
    }
}
