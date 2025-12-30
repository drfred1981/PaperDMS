package fr.smartprod.paperdms.search.service.mapper;

import static fr.smartprod.paperdms.search.domain.SearchFacetAsserts.*;
import static fr.smartprod.paperdms.search.domain.SearchFacetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchFacetMapperTest {

    private SearchFacetMapper searchFacetMapper;

    @BeforeEach
    void setUp() {
        searchFacetMapper = new SearchFacetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSearchFacetSample1();
        var actual = searchFacetMapper.toEntity(searchFacetMapper.toDto(expected));
        assertSearchFacetAllPropertiesEquals(expected, actual);
    }
}
