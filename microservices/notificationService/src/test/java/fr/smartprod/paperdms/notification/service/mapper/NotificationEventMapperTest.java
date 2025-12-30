package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.NotificationEventAsserts.*;
import static fr.smartprod.paperdms.notification.domain.NotificationEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationEventMapperTest {

    private NotificationEventMapper notificationEventMapper;

    @BeforeEach
    void setUp() {
        notificationEventMapper = new NotificationEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationEventSample1();
        var actual = notificationEventMapper.toEntity(notificationEventMapper.toDto(expected));
        assertNotificationEventAllPropertiesEquals(expected, actual);
    }
}
