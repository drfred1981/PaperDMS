package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingDashboardMapperTest {

    private ReportingDashboardMapper reportingDashboardMapper;

    @BeforeEach
    void setUp() {
        reportingDashboardMapper = new ReportingDashboardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingDashboardSample1();
        var actual = reportingDashboardMapper.toEntity(reportingDashboardMapper.toDto(expected));
        assertReportingDashboardAllPropertiesEquals(expected, actual);
    }
}
