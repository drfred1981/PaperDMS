package fr.smartprod.paperdms.similarity.service.mapper;

import static fr.smartprod.paperdms.similarity.domain.SimilarityClusterAsserts.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityClusterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimilarityClusterMapperTest {

    private SimilarityClusterMapper similarityClusterMapper;

    @BeforeEach
    void setUp() {
        similarityClusterMapper = new SimilarityClusterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSimilarityClusterSample1();
        var actual = similarityClusterMapper.toEntity(similarityClusterMapper.toDto(expected));
        assertSimilarityClusterAllPropertiesEquals(expected, actual);
    }
}
