package fr.smartprod.paperdms.search.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemanticSearchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemanticSearchDTO.class);
        SemanticSearchDTO semanticSearchDTO1 = new SemanticSearchDTO();
        semanticSearchDTO1.setId(1L);
        SemanticSearchDTO semanticSearchDTO2 = new SemanticSearchDTO();
        assertThat(semanticSearchDTO1).isNotEqualTo(semanticSearchDTO2);
        semanticSearchDTO2.setId(semanticSearchDTO1.getId());
        assertThat(semanticSearchDTO1).isEqualTo(semanticSearchDTO2);
        semanticSearchDTO2.setId(2L);
        assertThat(semanticSearchDTO1).isNotEqualTo(semanticSearchDTO2);
        semanticSearchDTO1.setId(null);
        assertThat(semanticSearchDTO1).isNotEqualTo(semanticSearchDTO2);
    }
}
