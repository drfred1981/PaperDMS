package com.ged.similarity.service.mapper;

import static com.ged.similarity.domain.DocumentFingerprintAsserts.*;
import static com.ged.similarity.domain.DocumentFingerprintTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentFingerprintMapperTest {

    private DocumentFingerprintMapper documentFingerprintMapper;

    @BeforeEach
    void setUp() {
        documentFingerprintMapper = new DocumentFingerprintMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentFingerprintSample1();
        var actual = documentFingerprintMapper.toEntity(documentFingerprintMapper.toDto(expected));
        assertDocumentFingerprintAllPropertiesEquals(expected, actual);
    }
}
