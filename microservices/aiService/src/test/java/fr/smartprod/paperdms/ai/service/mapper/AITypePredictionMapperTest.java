package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AITypePredictionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AITypePredictionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AITypePredictionMapperTest {

    private AITypePredictionMapper aITypePredictionMapper;

    @BeforeEach
    void setUp() {
        aITypePredictionMapper = new AITypePredictionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAITypePredictionSample1();
        var actual = aITypePredictionMapper.toEntity(aITypePredictionMapper.toDto(expected));
        assertAITypePredictionAllPropertiesEquals(expected, actual);
    }
}
