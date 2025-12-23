package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.SystemMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.SystemMetricTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemMetricMapperTest {

    private SystemMetricMapper systemMetricMapper;

    @BeforeEach
    void setUp() {
        systemMetricMapper = new SystemMetricMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemMetricSample1();
        var actual = systemMetricMapper.toEntity(systemMetricMapper.toDto(expected));
        assertSystemMetricAllPropertiesEquals(expected, actual);
    }
}
