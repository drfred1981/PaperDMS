package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.ScheduledReportAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.ScheduledReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduledReportMapperTest {

    private ScheduledReportMapper scheduledReportMapper;

    @BeforeEach
    void setUp() {
        scheduledReportMapper = new ScheduledReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScheduledReportSample1();
        var actual = scheduledReportMapper.toEntity(scheduledReportMapper.toDto(expected));
        assertScheduledReportAllPropertiesEquals(expected, actual);
    }
}
