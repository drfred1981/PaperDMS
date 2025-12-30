package fr.smartprod.paperdms.similarity.service.mapper;

import static fr.smartprod.paperdms.similarity.domain.SimilarityJobAsserts.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimilarityJobMapperTest {

    private SimilarityJobMapper similarityJobMapper;

    @BeforeEach
    void setUp() {
        similarityJobMapper = new SimilarityJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSimilarityJobSample1();
        var actual = similarityJobMapper.toEntity(similarityJobMapper.toDto(expected));
        assertSimilarityJobAllPropertiesEquals(expected, actual);
    }
}
