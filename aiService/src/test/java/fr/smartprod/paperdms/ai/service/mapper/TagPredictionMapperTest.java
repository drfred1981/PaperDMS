package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.TagPredictionAsserts.*;
import static fr.smartprod.paperdms.ai.domain.TagPredictionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagPredictionMapperTest {

    private TagPredictionMapper tagPredictionMapper;

    @BeforeEach
    void setUp() {
        tagPredictionMapper = new TagPredictionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTagPredictionSample1();
        var actual = tagPredictionMapper.toEntity(tagPredictionMapper.toDto(expected));
        assertTagPredictionAllPropertiesEquals(expected, actual);
    }
}
