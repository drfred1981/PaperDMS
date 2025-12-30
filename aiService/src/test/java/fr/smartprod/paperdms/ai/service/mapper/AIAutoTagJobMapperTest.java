package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AIAutoTagJobMapperTest {

    private AIAutoTagJobMapper aIAutoTagJobMapper;

    @BeforeEach
    void setUp() {
        aIAutoTagJobMapper = new AIAutoTagJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAIAutoTagJobSample1();
        var actual = aIAutoTagJobMapper.toEntity(aIAutoTagJobMapper.toDto(expected));
        assertAIAutoTagJobAllPropertiesEquals(expected, actual);
    }
}
