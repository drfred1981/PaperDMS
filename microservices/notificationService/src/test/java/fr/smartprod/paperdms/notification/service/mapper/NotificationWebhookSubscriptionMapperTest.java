package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscriptionAsserts.*;
import static fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationWebhookSubscriptionMapperTest {

    private NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper;

    @BeforeEach
    void setUp() {
        notificationWebhookSubscriptionMapper = new NotificationWebhookSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationWebhookSubscriptionSample1();
        var actual = notificationWebhookSubscriptionMapper.toEntity(notificationWebhookSubscriptionMapper.toDto(expected));
        assertNotificationWebhookSubscriptionAllPropertiesEquals(expected, actual);
    }
}
