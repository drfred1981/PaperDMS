package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.OcrResultAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.OcrResultTestSamples.*;

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
