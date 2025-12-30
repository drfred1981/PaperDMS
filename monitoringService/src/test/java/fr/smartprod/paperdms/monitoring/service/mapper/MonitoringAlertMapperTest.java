package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringAlertMapperTest {

    private MonitoringAlertMapper monitoringAlertMapper;

    @BeforeEach
    void setUp() {
        monitoringAlertMapper = new MonitoringAlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringAlertSample1();
        var actual = monitoringAlertMapper.toEntity(monitoringAlertMapper.toDto(expected));
        assertMonitoringAlertAllPropertiesEquals(expected, actual);
    }
}
