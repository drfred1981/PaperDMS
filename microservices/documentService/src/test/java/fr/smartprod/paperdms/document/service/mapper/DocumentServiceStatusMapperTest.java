package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentServiceStatusAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentServiceStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentServiceStatusMapperTest {

    private DocumentServiceStatusMapper documentServiceStatusMapper;

    @BeforeEach
    void setUp() {
        documentServiceStatusMapper = new DocumentServiceStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentServiceStatusSample1();
        var actual = documentServiceStatusMapper.toEntity(documentServiceStatusMapper.toDto(expected));
        assertDocumentServiceStatusAllPropertiesEquals(expected, actual);
    }
}
