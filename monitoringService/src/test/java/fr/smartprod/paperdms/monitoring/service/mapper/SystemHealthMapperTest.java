package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.SystemHealthAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.SystemHealthTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemHealthMapperTest {

    private SystemHealthMapper systemHealthMapper;

    @BeforeEach
    void setUp() {
        systemHealthMapper = new SystemHealthMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemHealthSample1();
        var actual = systemHealthMapper.toEntity(systemHealthMapper.toDto(expected));
        assertSystemHealthAllPropertiesEquals(expected, actual);
    }
}
