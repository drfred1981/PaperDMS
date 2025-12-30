package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetricTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingPerformanceMetricMapperTest {

    private ReportingPerformanceMetricMapper reportingPerformanceMetricMapper;

    @BeforeEach
    void setUp() {
        reportingPerformanceMetricMapper = new ReportingPerformanceMetricMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingPerformanceMetricSample1();
        var actual = reportingPerformanceMetricMapper.toEntity(reportingPerformanceMetricMapper.toDto(expected));
        assertReportingPerformanceMetricAllPropertiesEquals(expected, actual);
    }
}
