package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SearchQueryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
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
}
