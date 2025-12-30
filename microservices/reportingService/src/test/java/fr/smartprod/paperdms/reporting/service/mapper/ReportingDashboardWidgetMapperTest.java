package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidgetAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidgetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingDashboardWidgetMapperTest {

    private ReportingDashboardWidgetMapper reportingDashboardWidgetMapper;

    @BeforeEach
    void setUp() {
        reportingDashboardWidgetMapper = new ReportingDashboardWidgetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingDashboardWidgetSample1();
        var actual = reportingDashboardWidgetMapper.toEntity(reportingDashboardWidgetMapper.toDto(expected));
        assertReportingDashboardWidgetAllPropertiesEquals(expected, actual);
    }
}
