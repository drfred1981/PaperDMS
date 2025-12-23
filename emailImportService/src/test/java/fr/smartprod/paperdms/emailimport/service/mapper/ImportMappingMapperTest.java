package fr.smartprod.paperdms.emailimport.service.mapper;

import static fr.smartprod.paperdms.emailimport.domain.ImportMappingAsserts.*;
import static fr.smartprod.paperdms.emailimport.domain.ImportMappingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImportMappingMapperTest {

    private ImportMappingMapper importMappingMapper;

    @BeforeEach
    void setUp() {
        importMappingMapper = new ImportMappingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImportMappingSample1();
        var actual = importMappingMapper.toEntity(importMappingMapper.toDto(expected));
        assertImportMappingAllPropertiesEquals(expected, actual);
    }
}
