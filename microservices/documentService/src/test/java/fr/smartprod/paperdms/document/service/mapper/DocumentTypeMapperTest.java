package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentTypeAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTypeMapperTest {

    private DocumentTypeMapper documentTypeMapper;

    @BeforeEach
    void setUp() {
        documentTypeMapper = new DocumentTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentTypeSample1();
        var actual = documentTypeMapper.toEntity(documentTypeMapper.toDto(expected));
        assertDocumentTypeAllPropertiesEquals(expected, actual);
    }
}
