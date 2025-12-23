package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.PerformanceMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.PerformanceMetricTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerformanceMetricMapperTest {

    private PerformanceMetricMapper performanceMetricMapper;

    @BeforeEach
    void setUp() {
        performanceMetricMapper = new PerformanceMetricMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPerformanceMetricSample1();
        var actual = performanceMetricMapper.toEntity(performanceMetricMapper.toDto(expected));
        assertPerformanceMetricAllPropertiesEquals(expected, actual);
    }
}
