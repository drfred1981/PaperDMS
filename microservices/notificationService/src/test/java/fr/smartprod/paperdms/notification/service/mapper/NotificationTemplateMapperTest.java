package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.NotificationTemplateAsserts.*;
import static fr.smartprod.paperdms.notification.domain.NotificationTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationTemplateMapperTest {

    private NotificationTemplateMapper notificationTemplateMapper;

    @BeforeEach
    void setUp() {
        notificationTemplateMapper = new NotificationTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationTemplateSample1();
        var actual = notificationTemplateMapper.toEntity(notificationTemplateMapper.toDto(expected));
        assertNotificationTemplateAllPropertiesEquals(expected, actual);
    }
}
