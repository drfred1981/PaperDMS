package fr.smartprod.paperdms.similarity.service.mapper;

import static fr.smartprod.paperdms.similarity.domain.DocumentSimilarityAsserts.*;
import static fr.smartprod.paperdms.similarity.domain.DocumentSimilarityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentSimilarityMapperTest {

    private DocumentSimilarityMapper documentSimilarityMapper;

    @BeforeEach
    void setUp() {
        documentSimilarityMapper = new DocumentSimilarityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentSimilaritySample1();
        var actual = documentSimilarityMapper.toEntity(documentSimilarityMapper.toDto(expected));
        assertDocumentSimilarityAllPropertiesEquals(expected, actual);
    }
}
