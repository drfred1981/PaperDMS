package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentVersionAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentVersionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentVersionMapperTest {

    private DocumentVersionMapper documentVersionMapper;

    @BeforeEach
    void setUp() {
        documentVersionMapper = new DocumentVersionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentVersionSample1();
        var actual = documentVersionMapper.toEntity(documentVersionMapper.toDto(expected));
        assertDocumentVersionAllPropertiesEquals(expected, actual);
    }
}
