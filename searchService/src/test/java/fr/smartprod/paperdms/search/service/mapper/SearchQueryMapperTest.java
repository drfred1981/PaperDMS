package fr.smartprod.paperdms.search.service.mapper;

import static fr.smartprod.paperdms.search.domain.SearchQueryAsserts.*;
import static fr.smartprod.paperdms.search.domain.SearchQueryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchQueryMapperTest {

    private SearchQueryMapper searchQueryMapper;

    @BeforeEach
    void setUp() {
        searchQueryMapper = new SearchQueryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSearchQuerySample1();
        var actual = searchQueryMapper.toEntity(searchQueryMapper.toDto(expected));
        assertSearchQueryAllPropertiesEquals(expected, actual);
    }
}
