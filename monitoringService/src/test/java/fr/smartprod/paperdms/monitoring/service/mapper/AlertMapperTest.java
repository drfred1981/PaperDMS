package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.AlertAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.AlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlertMapperTest {

    private AlertMapper alertMapper;

    @BeforeEach
    void setUp() {
        alertMapper = new AlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlertSample1();
        var actual = alertMapper.toEntity(alertMapper.toDto(expected));
        assertAlertAllPropertiesEquals(expected, actual);
    }
}
