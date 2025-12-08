package com.ged.notification.domain;

import static com.ged.notification.domain.NotificationTemplateTestSamples.*;
import static com.ged.notification.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void templateTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        NotificationTemplate notificationTemplateBack = getNotificationTemplateRandomSampleGenerator();

        notification.setTemplate(notificationTemplateBack);
        assertThat(notification.getTemplate()).isEqualTo(notificationTemplateBack);

        notification.template(null);
        assertThat(notification.getTemplate()).isNull();
    }
}
