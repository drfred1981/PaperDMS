package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingScheduledReportAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingScheduledReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingScheduledReportMapperTest {

    private ReportingScheduledReportMapper reportingScheduledReportMapper;

    @BeforeEach
    void setUp() {
        reportingScheduledReportMapper = new ReportingScheduledReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingScheduledReportSample1();
        var actual = reportingScheduledReportMapper.toEntity(reportingScheduledReportMapper.toDto(expected));
        assertReportingScheduledReportAllPropertiesEquals(expected, actual);
    }
}
