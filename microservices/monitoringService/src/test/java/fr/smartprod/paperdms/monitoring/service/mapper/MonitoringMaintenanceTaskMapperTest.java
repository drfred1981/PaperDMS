package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTaskAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTaskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringMaintenanceTaskMapperTest {

    private MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper;

    @BeforeEach
    void setUp() {
        monitoringMaintenanceTaskMapper = new MonitoringMaintenanceTaskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringMaintenanceTaskSample1();
        var actual = monitoringMaintenanceTaskMapper.toEntity(monitoringMaintenanceTaskMapper.toDto(expected));
        assertMonitoringMaintenanceTaskAllPropertiesEquals(expected, actual);
    }
}
