package fr.smartprod.paperdms.similarity.service.mapper;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparisonAsserts.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparisonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimilarityDocumentComparisonMapperTest {

    private SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper;

    @BeforeEach
    void setUp() {
        similarityDocumentComparisonMapper = new SimilarityDocumentComparisonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSimilarityDocumentComparisonSample1();
        var actual = similarityDocumentComparisonMapper.toEntity(similarityDocumentComparisonMapper.toDto(expected));
        assertSimilarityDocumentComparisonAllPropertiesEquals(expected, actual);
    }
}
