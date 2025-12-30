package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ReportingExecutionAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingExecutionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingExecutionMapperTest {

    private ReportingExecutionMapper reportingExecutionMapper;

    @BeforeEach
    void setUp() {
        reportingExecutionMapper = new ReportingExecutionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportingExecutionSample1();
        var actual = reportingExecutionMapper.toEntity(reportingExecutionMapper.toDto(expected));
        assertReportingExecutionAllPropertiesEquals(expected, actual);
    }
}
