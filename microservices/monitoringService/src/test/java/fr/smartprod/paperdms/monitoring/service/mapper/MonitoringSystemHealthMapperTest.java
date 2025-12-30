package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealthAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealthTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringSystemHealthMapperTest {

    private MonitoringSystemHealthMapper monitoringSystemHealthMapper;

    @BeforeEach
    void setUp() {
        monitoringSystemHealthMapper = new MonitoringSystemHealthMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringSystemHealthSample1();
        var actual = monitoringSystemHealthMapper.toEntity(monitoringSystemHealthMapper.toDto(expected));
        assertMonitoringSystemHealthAllPropertiesEquals(expected, actual);
    }
}
