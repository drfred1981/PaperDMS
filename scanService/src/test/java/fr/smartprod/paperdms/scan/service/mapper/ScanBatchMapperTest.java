package fr.smartprod.paperdms.scan.service.mapper;

import static fr.smartprod.paperdms.scan.domain.ScanBatchAsserts.*;
import static fr.smartprod.paperdms.scan.domain.ScanBatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScanBatchMapperTest {

    private ScanBatchMapper scanBatchMapper;

    @BeforeEach
    void setUp() {
        scanBatchMapper = new ScanBatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScanBatchSample1();
        var actual = scanBatchMapper.toEntity(scanBatchMapper.toDto(expected));
        assertScanBatchAllPropertiesEquals(expected, actual);
    }
}
