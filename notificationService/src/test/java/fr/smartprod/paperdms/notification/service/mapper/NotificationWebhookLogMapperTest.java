package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookLogAsserts.*;
import static fr.smartprod.paperdms.notification.domain.NotificationWebhookLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationWebhookLogMapperTest {

    private NotificationWebhookLogMapper notificationWebhookLogMapper;

    @BeforeEach
    void setUp() {
        notificationWebhookLogMapper = new NotificationWebhookLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationWebhookLogSample1();
        var actual = notificationWebhookLogMapper.toEntity(notificationWebhookLogMapper.toDto(expected));
        assertNotificationWebhookLogAllPropertiesEquals(expected, actual);
    }
}
