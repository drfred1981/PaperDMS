package fr.smartprod.paperdms.export.service.mapper;

import static fr.smartprod.paperdms.export.domain.ExportJobAsserts.*;
import static fr.smartprod.paperdms.export.domain.ExportJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExportJobMapperTest {

    private ExportJobMapper exportJobMapper;

    @BeforeEach
    void setUp() {
        exportJobMapper = new ExportJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExportJobSample1();
        var actual = exportJobMapper.toEntity(exportJobMapper.toDto(expected));
        assertExportJobAllPropertiesEquals(expected, actual);
    }
}
