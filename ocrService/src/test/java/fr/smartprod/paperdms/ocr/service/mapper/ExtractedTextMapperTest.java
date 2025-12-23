package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.ExtractedTextAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.ExtractedTextTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtractedTextMapperTest {

    private ExtractedTextMapper extractedTextMapper;

    @BeforeEach
    void setUp() {
        extractedTextMapper = new ExtractedTextMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExtractedTextSample1();
        var actual = extractedTextMapper.toEntity(extractedTextMapper.toDto(expected));
        assertExtractedTextAllPropertiesEquals(expected, actual);
    }
}
