package fr.smartprod.paperdms.search.domain;

import static fr.smartprod.paperdms.search.domain.SemanticSearchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemanticSearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemanticSearch.class);
        SemanticSearch semanticSearch1 = getSemanticSearchSample1();
        SemanticSearch semanticSearch2 = new SemanticSearch();
        assertThat(semanticSearch1).isNotEqualTo(semanticSearch2);

        semanticSearch2.setId(semanticSearch1.getId());
        assertThat(semanticSearch1).isEqualTo(semanticSearch2);

        semanticSearch2 = getSemanticSearchSample2();
        assertThat(semanticSearch1).isNotEqualTo(semanticSearch2);
    }
}
