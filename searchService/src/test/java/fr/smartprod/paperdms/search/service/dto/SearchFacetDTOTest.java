package fr.smartprod.paperdms.search.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchFacetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchFacetDTO.class);
        SearchFacetDTO searchFacetDTO1 = new SearchFacetDTO();
        searchFacetDTO1.setId(1L);
        SearchFacetDTO searchFacetDTO2 = new SearchFacetDTO();
        assertThat(searchFacetDTO1).isNotEqualTo(searchFacetDTO2);
        searchFacetDTO2.setId(searchFacetDTO1.getId());
        assertThat(searchFacetDTO1).isEqualTo(searchFacetDTO2);
        searchFacetDTO2.setId(2L);
        assertThat(searchFacetDTO1).isNotEqualTo(searchFacetDTO2);
        searchFacetDTO1.setId(null);
        assertThat(searchFacetDTO1).isNotEqualTo(searchFacetDTO2);
    }
}
