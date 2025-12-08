package com.ged.ai.service.mapper;

import static com.ged.ai.domain.CorrespondentExtractionAsserts.*;
import static com.ged.ai.domain.CorrespondentExtractionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrespondentExtractionMapperTest {

    private CorrespondentExtractionMapper correspondentExtractionMapper;

    @BeforeEach
    void setUp() {
        correspondentExtractionMapper = new CorrespondentExtractionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCorrespondentExtractionSample1();
        var actual = correspondentExtractionMapper.toEntity(correspondentExtractionMapper.toDto(expected));
        assertCorrespondentExtractionAllPropertiesEquals(expected, actual);
    }
}
