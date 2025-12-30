package fr.smartprod.paperdms.notification.domain;

import static fr.smartprod.paperdms.notification.domain.NotificationTemplateTestSamples.*;
import static fr.smartprod.paperdms.notification.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.notification.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NotificationTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationTemplate.class);
        NotificationTemplate notificationTemplate1 = getNotificationTemplateSample1();
        NotificationTemplate notificationTemplate2 = new NotificationTemplate();
        assertThat(notificationTemplate1).isNotEqualTo(notificationTemplate2);

        notificationTemplate2.setId(notificationTemplate1.getId());
        assertThat(notificationTemplate1).isEqualTo(notificationTemplate2);

        notificationTemplate2 = getNotificationTemplateSample2();
        assertThat(notificationTemplate1).isNotEqualTo(notificationTemplate2);
    }

    @Test
    void notificationsTest() {
        NotificationTemplate notificationTemplate = getNotificationTemplateRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        notificationTemplate.addNotifications(notificationBack);
        assertThat(notificationTemplate.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTemplate()).isEqualTo(notificationTemplate);

        notificationTemplate.removeNotifications(notificationBack);
        assertThat(notificationTemplate.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTemplate()).isNull();

        notificationTemplate.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(notificationTemplate.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTemplate()).isEqualTo(notificationTemplate);

        notificationTemplate.setNotifications(new HashSet<>());
        assertThat(notificationTemplate.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTemplate()).isNull();
    }
}
