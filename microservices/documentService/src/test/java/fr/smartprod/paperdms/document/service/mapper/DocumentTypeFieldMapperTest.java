package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTypeFieldMapperTest {

    private DocumentTypeFieldMapper documentTypeFieldMapper;

    @BeforeEach
    void setUp() {
        documentTypeFieldMapper = new DocumentTypeFieldMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentTypeFieldSample1();
        var actual = documentTypeFieldMapper.toEntity(documentTypeFieldMapper.toDto(expected));
        assertDocumentTypeFieldAllPropertiesEquals(expected, actual);
    }
}
