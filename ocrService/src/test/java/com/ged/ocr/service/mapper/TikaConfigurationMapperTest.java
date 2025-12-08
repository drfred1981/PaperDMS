package com.ged.ocr.service.mapper;

import static com.ged.ocr.domain.TikaConfigurationAsserts.*;
import static com.ged.ocr.domain.TikaConfigurationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TikaConfigurationMapperTest {

    private TikaConfigurationMapper tikaConfigurationMapper;

    @BeforeEach
    void setUp() {
        tikaConfigurationMapper = new TikaConfigurationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTikaConfigurationSample1();
        var actual = tikaConfigurationMapper.toEntity(tikaConfigurationMapper.toDto(expected));
        assertTikaConfigurationAllPropertiesEquals(expected, actual);
    }
}
