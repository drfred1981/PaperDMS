package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AILanguageDetectionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AILanguageDetectionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AILanguageDetectionMapperTest {

    private AILanguageDetectionMapper aILanguageDetectionMapper;

    @BeforeEach
    void setUp() {
        aILanguageDetectionMapper = new AILanguageDetectionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAILanguageDetectionSample1();
        var actual = aILanguageDetectionMapper.toEntity(aILanguageDetectionMapper.toDto(expected));
        assertAILanguageDetectionAllPropertiesEquals(expected, actual);
    }
}
