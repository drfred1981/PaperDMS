package fr.smartprod.paperdms.similarity.service.mapper;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprintAsserts.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprintTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimilarityDocumentFingerprintMapperTest {

    private SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper;

    @BeforeEach
    void setUp() {
        similarityDocumentFingerprintMapper = new SimilarityDocumentFingerprintMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSimilarityDocumentFingerprintSample1();
        var actual = similarityDocumentFingerprintMapper.toEntity(similarityDocumentFingerprintMapper.toDto(expected));
        assertSimilarityDocumentFingerprintAllPropertiesEquals(expected, actual);
    }
}
