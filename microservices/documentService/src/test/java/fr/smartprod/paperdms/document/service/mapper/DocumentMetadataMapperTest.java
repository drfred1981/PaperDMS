package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentMetadataAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentMetadataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentMetadataMapperTest {

    private DocumentMetadataMapper documentMetadataMapper;

    @BeforeEach
    void setUp() {
        documentMetadataMapper = new DocumentMetadataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentMetadataSample1();
        var actual = documentMetadataMapper.toEntity(documentMetadataMapper.toDto(expected));
        assertDocumentMetadataAllPropertiesEquals(expected, actual);
    }
}
