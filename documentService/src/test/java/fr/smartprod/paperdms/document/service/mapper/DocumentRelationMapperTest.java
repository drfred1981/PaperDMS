package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentRelationAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentRelationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentRelationMapperTest {

    private DocumentRelationMapper documentRelationMapper;

    @BeforeEach
    void setUp() {
        documentRelationMapper = new DocumentRelationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentRelationSample1();
        var actual = documentRelationMapper.toEntity(documentRelationMapper.toDto(expected));
        assertDocumentRelationAllPropertiesEquals(expected, actual);
    }
}
