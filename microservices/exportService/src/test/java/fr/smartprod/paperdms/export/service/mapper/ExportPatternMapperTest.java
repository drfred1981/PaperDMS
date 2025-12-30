package fr.smartprod.paperdms.export.service.mapper;

import static fr.smartprod.paperdms.export.domain.ExportPatternAsserts.*;
import static fr.smartprod.paperdms.export.domain.ExportPatternTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExportPatternMapperTest {

    private ExportPatternMapper exportPatternMapper;

    @BeforeEach
    void setUp() {
        exportPatternMapper = new ExportPatternMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExportPatternSample1();
        var actual = exportPatternMapper.toEntity(exportPatternMapper.toDto(expected));
        assertExportPatternAllPropertiesEquals(expected, actual);
    }
}
