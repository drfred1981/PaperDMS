package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentStatisticsAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentStatisticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentStatisticsMapperTest {

    private DocumentStatisticsMapper documentStatisticsMapper;

    @BeforeEach
    void setUp() {
        documentStatisticsMapper = new DocumentStatisticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentStatisticsSample1();
        var actual = documentStatisticsMapper.toEntity(documentStatisticsMapper.toDto(expected));
        assertDocumentStatisticsAllPropertiesEquals(expected, actual);
    }
}
