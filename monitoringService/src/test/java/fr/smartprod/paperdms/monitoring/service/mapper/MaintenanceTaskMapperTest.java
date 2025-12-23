package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.MaintenanceTaskAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.MaintenanceTaskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintenanceTaskMapperTest {

    private MaintenanceTaskMapper maintenanceTaskMapper;

    @BeforeEach
    void setUp() {
        maintenanceTaskMapper = new MaintenanceTaskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaintenanceTaskSample1();
        var actual = maintenanceTaskMapper.toEntity(maintenanceTaskMapper.toDto(expected));
        assertMaintenanceTaskAllPropertiesEquals(expected, actual);
    }
}
