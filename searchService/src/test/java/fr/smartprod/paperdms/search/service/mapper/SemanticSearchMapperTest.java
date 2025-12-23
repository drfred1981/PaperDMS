package fr.smartprod.paperdms.search.service.mapper;

import static fr.smartprod.paperdms.search.domain.SemanticSearchAsserts.*;
import static fr.smartprod.paperdms.search.domain.SemanticSearchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemanticSearchMapperTest {

    private SemanticSearchMapper semanticSearchMapper;

    @BeforeEach
    void setUp() {
        semanticSearchMapper = new SemanticSearchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSemanticSearchSample1();
        var actual = semanticSearchMapper.toEntity(semanticSearchMapper.toDto(expected));
        assertSemanticSearchAllPropertiesEquals(expected, actual);
    }
}
