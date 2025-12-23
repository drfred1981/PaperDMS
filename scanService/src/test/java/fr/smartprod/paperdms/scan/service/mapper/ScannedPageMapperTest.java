package fr.smartprod.paperdms.scan.service.mapper;

import static fr.smartprod.paperdms.scan.domain.ScannedPageAsserts.*;
import static fr.smartprod.paperdms.scan.domain.ScannedPageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScannedPageMapperTest {

    private ScannedPageMapper scannedPageMapper;

    @BeforeEach
    void setUp() {
        scannedPageMapper = new ScannedPageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScannedPageSample1();
        var actual = scannedPageMapper.toEntity(scannedPageMapper.toDto(expected));
        assertScannedPageAllPropertiesEquals(expected, actual);
    }
}
