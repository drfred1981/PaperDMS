package fr.smartprod.paperdms.search.service.mapper;

import static fr.smartprod.paperdms.search.domain.SearchIndexAsserts.*;
import static fr.smartprod.paperdms.search.domain.SearchIndexTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchIndexMapperTest {

    private SearchIndexMapper searchIndexMapper;

    @BeforeEach
    void setUp() {
        searchIndexMapper = new SearchIndexMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSearchIndexSample1();
        var actual = searchIndexMapper.toEntity(searchIndexMapper.toDto(expected));
        assertSearchIndexAllPropertiesEquals(expected, actual);
    }
}
