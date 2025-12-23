package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.ExtractedFieldAsserts.*;
import static fr.smartprod.paperdms.document.domain.ExtractedFieldTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtractedFieldMapperTest {

    private ExtractedFieldMapper extractedFieldMapper;

    @BeforeEach
    void setUp() {
        extractedFieldMapper = new ExtractedFieldMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExtractedFieldSample1();
        var actual = extractedFieldMapper.toEntity(extractedFieldMapper.toDto(expected));
        assertExtractedFieldAllPropertiesEquals(expected, actual);
    }
}
