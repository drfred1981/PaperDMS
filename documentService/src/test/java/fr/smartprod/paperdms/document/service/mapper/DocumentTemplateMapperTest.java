package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentTemplateAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTemplateMapperTest {

    private DocumentTemplateMapper documentTemplateMapper;

    @BeforeEach
    void setUp() {
        documentTemplateMapper = new DocumentTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentTemplateSample1();
        var actual = documentTemplateMapper.toEntity(documentTemplateMapper.toDto(expected));
        assertDocumentTemplateAllPropertiesEquals(expected, actual);
    }
}
