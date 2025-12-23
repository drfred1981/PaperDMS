package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AiCacheAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AiCacheTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AiCacheMapperTest {

    private AiCacheMapper aiCacheMapper;

    @BeforeEach
    void setUp() {
        aiCacheMapper = new AiCacheMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAiCacheSample1();
        var actual = aiCacheMapper.toEntity(aiCacheMapper.toDto(expected));
        assertAiCacheAllPropertiesEquals(expected, actual);
    }
}
