package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.OcrComparisonAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.OcrComparisonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OcrComparisonMapperTest {

    private OcrComparisonMapper ocrComparisonMapper;

    @BeforeEach
    void setUp() {
        ocrComparisonMapper = new OcrComparisonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOcrComparisonSample1();
        var actual = ocrComparisonMapper.toEntity(ocrComparisonMapper.toDto(expected));
        assertOcrComparisonAllPropertiesEquals(expected, actual);
    }
}
