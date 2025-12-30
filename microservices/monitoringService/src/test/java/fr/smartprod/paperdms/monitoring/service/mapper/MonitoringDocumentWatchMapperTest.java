package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatchAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoringDocumentWatchMapperTest {

    private MonitoringDocumentWatchMapper monitoringDocumentWatchMapper;

    @BeforeEach
    void setUp() {
        monitoringDocumentWatchMapper = new MonitoringDocumentWatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitoringDocumentWatchSample1();
        var actual = monitoringDocumentWatchMapper.toEntity(monitoringDocumentWatchMapper.toDto(expected));
        assertMonitoringDocumentWatchAllPropertiesEquals(expected, actual);
    }
}
