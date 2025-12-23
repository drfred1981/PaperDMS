package fr.smartprod.paperdms.scan.service.mapper;

import static fr.smartprod.paperdms.scan.domain.ScanJobAsserts.*;
import static fr.smartprod.paperdms.scan.domain.ScanJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScanJobMapperTest {

    private ScanJobMapper scanJobMapper;

    @BeforeEach
    void setUp() {
        scanJobMapper = new ScanJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScanJobSample1();
        var actual = scanJobMapper.toEntity(scanJobMapper.toDto(expected));
        assertScanJobAllPropertiesEquals(expected, actual);
    }
}
