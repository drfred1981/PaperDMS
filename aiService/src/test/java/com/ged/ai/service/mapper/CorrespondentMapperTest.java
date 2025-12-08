package com.ged.ai.service.mapper;

import static com.ged.ai.domain.CorrespondentAsserts.*;
import static com.ged.ai.domain.CorrespondentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrespondentMapperTest {

    private CorrespondentMapper correspondentMapper;

    @BeforeEach
    void setUp() {
        correspondentMapper = new CorrespondentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCorrespondentSample1();
        var actual = correspondentMapper.toEntity(correspondentMapper.toDto(expected));
        assertCorrespondentAllPropertiesEquals(expected, actual);
    }
}
