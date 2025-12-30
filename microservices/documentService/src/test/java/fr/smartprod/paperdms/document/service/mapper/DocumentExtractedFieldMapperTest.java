package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentExtractedFieldAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentExtractedFieldTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentExtractedFieldMapperTest {

    private DocumentExtractedFieldMapper documentExtractedFieldMapper;

    @BeforeEach
    void setUp() {
        documentExtractedFieldMapper = new DocumentExtractedFieldMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentExtractedFieldSample1();
        var actual = documentExtractedFieldMapper.toEntity(documentExtractedFieldMapper.toDto(expected));
        assertDocumentExtractedFieldAllPropertiesEquals(expected, actual);
    }
}
