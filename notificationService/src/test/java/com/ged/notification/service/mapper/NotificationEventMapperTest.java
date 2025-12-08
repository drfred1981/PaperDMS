package com.ged.notification.service.mapper;

import static com.ged.notification.domain.NotificationEventAsserts.*;
import static com.ged.notification.domain.NotificationEventTestSamples.*;

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
