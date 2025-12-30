package fr.smartprod.paperdms.export.service.mapper;

import static fr.smartprod.paperdms.export.domain.ExportResultAsserts.*;
import static fr.smartprod.paperdms.export.domain.ExportResultTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExportResultMapperTest {

    private ExportResultMapper exportResultMapper;

    @BeforeEach
    void setUp() {
        exportResultMapper = new ExportResultMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExportResultSample1();
        var actual = exportResultMapper.toEntity(exportResultMapper.toDto(expected));
        assertExportResultAllPropertiesEquals(expected, actual);
    }
}
