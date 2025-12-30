package fr.smartprod.paperdms.search.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchSemanticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchSemanticDTO.class);
        SearchSemanticDTO searchSemanticDTO1 = new SearchSemanticDTO();
        searchSemanticDTO1.setId(1L);
        SearchSemanticDTO searchSemanticDTO2 = new SearchSemanticDTO();
        assertThat(searchSemanticDTO1).isNotEqualTo(searchSemanticDTO2);
        searchSemanticDTO2.setId(searchSemanticDTO1.getId());
        assertThat(searchSemanticDTO1).isEqualTo(searchSemanticDTO2);
        searchSemanticDTO2.setId(2L);
        assertThat(searchSemanticDTO1).isNotEqualTo(searchSemanticDTO2);
        searchSemanticDTO1.setId(null);
        assertThat(searchSemanticDTO1).isNotEqualTo(searchSemanticDTO2);
    }
}
