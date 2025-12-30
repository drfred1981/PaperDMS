package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.OcrJobAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.OcrJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OcrJobMapperTest {

    private OcrJobMapper ocrJobMapper;

    @BeforeEach
    void setUp() {
        ocrJobMapper = new OcrJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOcrJobSample1();
        var actual = ocrJobMapper.toEntity(ocrJobMapper.toDto(expected));
        assertOcrJobAllPropertiesEquals(expected, actual);
    }
}
