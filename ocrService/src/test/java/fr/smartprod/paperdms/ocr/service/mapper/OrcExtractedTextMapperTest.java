package fr.smartprod.paperdms.ocr.service.mapper;

import static fr.smartprod.paperdms.ocr.domain.OrcExtractedTextAsserts.*;
import static fr.smartprod.paperdms.ocr.domain.OrcExtractedTextTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrcExtractedTextMapperTest {

    private OrcExtractedTextMapper orcExtractedTextMapper;

    @BeforeEach
    void setUp() {
        orcExtractedTextMapper = new OrcExtractedTextMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrcExtractedTextSample1();
        var actual = orcExtractedTextMapper.toEntity(orcExtractedTextMapper.toDto(expected));
        assertOrcExtractedTextAllPropertiesEquals(expected, actual);
    }
}
