package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AICacheAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AICacheTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AICacheMapperTest {

    private AICacheMapper aICacheMapper;

    @BeforeEach
    void setUp() {
        aICacheMapper = new AICacheMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAICacheSample1();
        var actual = aICacheMapper.toEntity(aICacheMapper.toDto(expected));
        assertAICacheAllPropertiesEquals(expected, actual);
    }
}
