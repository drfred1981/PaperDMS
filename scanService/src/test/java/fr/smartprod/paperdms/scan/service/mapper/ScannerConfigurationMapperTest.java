package fr.smartprod.paperdms.scan.service.mapper;

import static fr.smartprod.paperdms.scan.domain.ScannerConfigurationAsserts.*;
import static fr.smartprod.paperdms.scan.domain.ScannerConfigurationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScannerConfigurationMapperTest {

    private ScannerConfigurationMapper scannerConfigurationMapper;

    @BeforeEach
    void setUp() {
        scannerConfigurationMapper = new ScannerConfigurationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScannerConfigurationSample1();
        var actual = scannerConfigurationMapper.toEntity(scannerConfigurationMapper.toDto(expected));
        assertScannerConfigurationAllPropertiesEquals(expected, actual);
    }
}
