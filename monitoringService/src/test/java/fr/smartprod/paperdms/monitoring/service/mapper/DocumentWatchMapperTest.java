package fr.smartprod.paperdms.monitoring.service.mapper;

import static fr.smartprod.paperdms.monitoring.domain.DocumentWatchAsserts.*;
import static fr.smartprod.paperdms.monitoring.domain.DocumentWatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentWatchMapperTest {

    private DocumentWatchMapper documentWatchMapper;

    @BeforeEach
    void setUp() {
        documentWatchMapper = new DocumentWatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentWatchSample1();
        var actual = documentWatchMapper.toEntity(documentWatchMapper.toDto(expected));
        assertDocumentWatchAllPropertiesEquals(expected, actual);
    }
}
