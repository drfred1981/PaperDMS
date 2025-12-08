package com.ged.ai.service.mapper;

import static com.ged.ai.domain.AutoTagJobAsserts.*;
import static com.ged.ai.domain.AutoTagJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutoTagJobMapperTest {

    private AutoTagJobMapper autoTagJobMapper;

    @BeforeEach
    void setUp() {
        autoTagJobMapper = new AutoTagJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAutoTagJobSample1();
        var actual = autoTagJobMapper.toEntity(autoTagJobMapper.toDto(expected));
        assertAutoTagJobAllPropertiesEquals(expected, actual);
    }
}
