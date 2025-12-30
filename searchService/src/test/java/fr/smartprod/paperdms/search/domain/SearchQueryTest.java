package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SearchFacetTestSamples.*;
import static fr.smartprod.paperdms.search.domain.SearchQueryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SearchQueryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchQuery.class);
        SearchQuery searchQuery1 = getSearchQuerySample1();
        SearchQuery searchQuery2 = new SearchQuery();
        assertThat(searchQuery1).isNotEqualTo(searchQuery2);

        searchQuery2.setId(searchQuery1.getId());
        assertThat(searchQuery1).isEqualTo(searchQuery2);

        searchQuery2 = getSearchQuerySample2();
        assertThat(searchQuery1).isNotEqualTo(searchQuery2);
    }

    @Test
    void facetsTest() {
        SearchQuery searchQuery = getSearchQueryRandomSampleGenerator();
        SearchFacet searchFacetBack = getSearchFacetRandomSampleGenerator();

        searchQuery.addFacets(searchFacetBack);
        assertThat(searchQuery.getFacets()).containsOnly(searchFacetBack);
        assertThat(searchFacetBack.getSearchQuery()).isEqualTo(searchQuery);

        searchQuery.removeFacets(searchFacetBack);
        assertThat(searchQuery.getFacets()).doesNotContain(searchFacetBack);
        assertThat(searchFacetBack.getSearchQuery()).isNull();

        searchQuery.facets(new HashSet<>(Set.of(searchFacetBack)));
        assertThat(searchQuery.getFacets()).containsOnly(searchFacetBack);
        assertThat(searchFacetBack.getSearchQuery()).isEqualTo(searchQuery);

        searchQuery.setFacets(new HashSet<>());
        assertThat(searchQuery.getFacets()).doesNotContain(searchFacetBack);
        assertThat(searchFacetBack.getSearchQuery()).isNull();
    }
}
