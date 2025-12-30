package fr.smartprod.paperdms.gateway.service.mapper;

import static fr.smartprod.paperdms.gateway.domain.DocumentProcessAsserts.*;
import static fr.smartprod.paperdms.gateway.domain.DocumentProcessTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentProcessMapperTest {

    private DocumentProcessMapper documentProcessMapper;

    @BeforeEach
    void setUp() {
        documentProcessMapper = new DocumentProcessMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentProcessSample1();
        var actual = documentProcessMapper.toEntity(documentProcessMapper.toDto(expected));
        assertDocumentProcessAllPropertiesEquals(expected, actual);
    }
}
