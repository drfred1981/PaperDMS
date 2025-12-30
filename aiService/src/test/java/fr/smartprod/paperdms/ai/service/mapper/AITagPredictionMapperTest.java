package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AITagPredictionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AITagPredictionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AITagPredictionMapperTest {

    private AITagPredictionMapper aITagPredictionMapper;

    @BeforeEach
    void setUp() {
        aITagPredictionMapper = new AITagPredictionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAITagPredictionSample1();
        var actual = aITagPredictionMapper.toEntity(aITagPredictionMapper.toDto(expected));
        assertAITagPredictionAllPropertiesEquals(expected, actual);
    }
}
