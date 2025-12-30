package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatusAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringServiceStatusMapperTest {

    private MonitoringServiceStatusMapper monitoringServiceStatusMapper;

    @BeforeEach
    void setUp() {
        monitoringServiceStatusMapper = new MonitoringServiceStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringServiceStatusSample1();
        var actual = monitoringServiceStatusMapper.toEntity(monitoringServiceStatusMapper.toDto(expected));
        assertMonitoringServiceStatusAllPropertiesEquals(expected, actual);
    }
}
