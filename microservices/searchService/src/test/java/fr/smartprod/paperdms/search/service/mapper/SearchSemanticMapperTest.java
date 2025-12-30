package fr.smartprod.paperdms.search.service.mapper;

import static fr.smartprod.paperdms.search.domain.SearchSemanticAsserts.*;
import static fr.smartprod.paperdms.search.domain.SearchSemanticTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchSemanticMapperTest {

    private SearchSemanticMapper searchSemanticMapper;

    @BeforeEach
    void setUp() {
        searchSemanticMapper = new SearchSemanticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSearchSemanticSample1();
        var actual = searchSemanticMapper.toEntity(searchSemanticMapper.toDto(expected));
        assertSearchSemanticAllPropertiesEquals(expected, actual);
    }
}
