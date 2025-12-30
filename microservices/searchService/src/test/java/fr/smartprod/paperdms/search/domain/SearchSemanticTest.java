package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SearchSemanticTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchSemanticTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchSemantic.class);
        SearchSemantic searchSemantic1 = getSearchSemanticSample1();
        SearchSemantic searchSemantic2 = new SearchSemantic();
        assertThat(searchSemantic1).isNotEqualTo(searchSemantic2);

        searchSemantic2.setId(searchSemantic1.getId());
        assertThat(searchSemantic1).isEqualTo(searchSemantic2);

        searchSemantic2 = getSearchSemanticSample2();
        assertThat(searchSemantic1).isNotEqualTo(searchSemantic2);
    }
}
