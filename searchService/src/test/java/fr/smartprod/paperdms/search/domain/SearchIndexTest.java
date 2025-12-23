package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SearchIndexTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchIndexTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchIndex.class);
        SearchIndex searchIndex1 = getSearchIndexSample1();
        SearchIndex searchIndex2 = new SearchIndex();
        assertThat(searchIndex1).isNotEqualTo(searchIndex2);

        searchIndex2.setId(searchIndex1.getId());
        assertThat(searchIndex1).isEqualTo(searchIndex2);

        searchIndex2 = getSearchIndexSample2();
        assertThat(searchIndex1).isNotEqualTo(searchIndex2);
    }
}
