package fr.smartprod.paperdms.notification.service.mapper;

import static fr.smartprod.paperdms.notification.domain.WebhookLogAsserts.*;
import static fr.smartprod.paperdms.notification.domain.WebhookLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebhookLogMapperTest {

    private WebhookLogMapper webhookLogMapper;

    @BeforeEach
    void setUp() {
        webhookLogMapper = new WebhookLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWebhookLogSample1();
        var actual = webhookLogMapper.toEntity(webhookLogMapper.toDto(expected));
        assertWebhookLogAllPropertiesEquals(expected, actual);
    }
}
