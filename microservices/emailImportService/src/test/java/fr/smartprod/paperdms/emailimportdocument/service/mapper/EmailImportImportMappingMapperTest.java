package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMappingAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMappingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailImportImportMappingMapperTest {

    private EmailImportImportMappingMapper emailImportImportMappingMapper;

    @BeforeEach
    void setUp() {
        emailImportImportMappingMapper = new EmailImportImportMappingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailImportImportMappingSample1();
        var actual = emailImportImportMappingMapper.toEntity(emailImportImportMappingMapper.toDto(expected));
        assertEmailImportImportMappingAllPropertiesEquals(expected, actual);
    }
}
