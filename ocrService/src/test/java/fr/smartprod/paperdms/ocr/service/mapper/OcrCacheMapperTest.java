package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.OcrCacheAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.OcrCacheTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OcrCacheMapperTest {

    private OcrCacheMapper ocrCacheMapper;

    @BeforeEach
    void setUp() {
        ocrCacheMapper = new OcrCacheMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOcrCacheSample1();
        var actual = ocrCacheMapper.toEntity(ocrCacheMapper.toDto(expected));
        assertOcrCacheAllPropertiesEquals(expected, actual);
    }
}
