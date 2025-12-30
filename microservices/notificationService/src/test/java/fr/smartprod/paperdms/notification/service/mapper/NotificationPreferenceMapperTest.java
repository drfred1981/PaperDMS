package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.NotificationPreferenceAsserts.*;
import static fr.smartprod.paperdms.notification.domain.NotificationPreferenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationPreferenceMapperTest {

    private NotificationPreferenceMapper notificationPreferenceMapper;

    @BeforeEach
    void setUp() {
        notificationPreferenceMapper = new NotificationPreferenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationPreferenceSample1();
        var actual = notificationPreferenceMapper.toEntity(notificationPreferenceMapper.toDto(expected));
        assertNotificationPreferenceAllPropertiesEquals(expected, actual);
    }
}
