package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AICorrespondentPredictionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AICorrespondentPredictionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AICorrespondentPredictionMapperTest {

    private AICorrespondentPredictionMapper aICorrespondentPredictionMapper;

    @BeforeEach
    void setUp() {
        aICorrespondentPredictionMapper = new AICorrespondentPredictionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAICorrespondentPredictionSample1();
        var actual = aICorrespondentPredictionMapper.toEntity(aICorrespondentPredictionMapper.toDto(expected));
        assertAICorrespondentPredictionAllPropertiesEquals(expected, actual);
    }
}
