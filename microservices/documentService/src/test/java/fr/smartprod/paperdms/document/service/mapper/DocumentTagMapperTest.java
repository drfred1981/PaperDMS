package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentTagAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentTagTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTagMapperTest {

    private DocumentTagMapper documentTagMapper;

    @BeforeEach
    void setUp() {
        documentTagMapper = new DocumentTagMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentTagSample1();
        var actual = documentTagMapper.toEntity(documentTagMapper.toDto(expected));
        assertDocumentTagAllPropertiesEquals(expected, actual);
    }
}
