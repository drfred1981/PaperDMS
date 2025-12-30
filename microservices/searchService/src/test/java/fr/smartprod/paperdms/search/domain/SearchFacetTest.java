package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SearchFacetTestSamples.*;
import static fr.smartprod.paperdms.search.domain.SearchQueryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchFacetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchFacet.class);
        SearchFacet searchFacet1 = getSearchFacetSample1();
        SearchFacet searchFacet2 = new SearchFacet();
        assertThat(searchFacet1).isNotEqualTo(searchFacet2);

        searchFacet2.setId(searchFacet1.getId());
        assertThat(searchFacet1).isEqualTo(searchFacet2);

        searchFacet2 = getSearchFacetSample2();
        assertThat(searchFacet1).isNotEqualTo(searchFacet2);
    }

    @Test
    void searchQueryTest() {
        SearchFacet searchFacet = getSearchFacetRandomSampleGenerator();
        SearchQuery searchQueryBack = getSearchQueryRandomSampleGenerator();

        searchFacet.setSearchQuery(searchQueryBack);
        assertThat(searchFacet.getSearchQuery()).isEqualTo(searchQueryBack);

        searchFacet.searchQuery(null);
        assertThat(searchFacet.getSearchQuery()).isNull();
    }
}
