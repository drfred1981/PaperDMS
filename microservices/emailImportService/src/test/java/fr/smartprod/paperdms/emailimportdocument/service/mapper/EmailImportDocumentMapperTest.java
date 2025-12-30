package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailImportDocumentMapperTest {

    private EmailImportDocumentMapper emailImportDocumentMapper;

    @BeforeEach
    void setUp() {
        emailImportDocumentMapper = new EmailImportDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailImportDocumentSample1();
        var actual = emailImportDocumentMapper.toEntity(emailImportDocumentMapper.toDto(expected));
        assertEmailImportDocumentAllPropertiesEquals(expected, actual);
    }
}
