package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.LanguageDetectionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.LanguageDetectionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LanguageDetectionMapperTest {

    private LanguageDetectionMapper languageDetectionMapper;

    @BeforeEach
    void setUp() {
        languageDetectionMapper = new LanguageDetectionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLanguageDetectionSample1();
        var actual = languageDetectionMapper.toEntity(languageDetectionMapper.toDto(expected));
        assertLanguageDetectionAllPropertiesEquals(expected, actual);
    }
}
