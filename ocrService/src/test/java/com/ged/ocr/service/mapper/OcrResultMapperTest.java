package com.ged.ocr.service.mapper;

import static com.ged.ocr.domain.OcrResultAsserts.*;
import static com.ged.ocr.domain.OcrResultTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OcrResultMapperTest {

    private OcrResultMapper ocrResultMapper;

    @BeforeEach
    void setUp() {
        ocrResultMapper = new OcrResultMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOcrResultSample1();
        var actual = ocrResultMapper.toEntity(ocrResultMapper.toDto(expected));
        assertOcrResultAllPropertiesEquals(expected, actual);
    }
}
