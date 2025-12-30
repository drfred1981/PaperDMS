package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingSystemMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingSystemMetricTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingSystemMetricMapperTest {

    private ReportingSystemMetricMapper reportingSystemMetricMapper;

    @BeforeEach
    void setUp() {
        reportingSystemMetricMapper = new ReportingSystemMetricMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingSystemMetricSample1();
        var actual = reportingSystemMetricMapper.toEntity(reportingSystemMetricMapper.toDto(expected));
        assertReportingSystemMetricAllPropertiesEquals(expected, actual);
    }
}
