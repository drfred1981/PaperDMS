package com.ged.search.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchQueryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchQueryDTO.class);
        SearchQueryDTO searchQueryDTO1 = new SearchQueryDTO();
        searchQueryDTO1.setId(1L);
        SearchQueryDTO searchQueryDTO2 = new SearchQueryDTO();
        assertThat(searchQueryDTO1).isNotEqualTo(searchQueryDTO2);
        searchQueryDTO2.setId(searchQueryDTO1.getId());
        assertThat(searchQueryDTO1).isEqualTo(searchQueryDTO2);
        searchQueryDTO2.setId(2L);
        assertThat(searchQueryDTO1).isNotEqualTo(searchQueryDTO2);
        searchQueryDTO1.setId(null);
        assertThat(searchQueryDTO1).isNotEqualTo(searchQueryDTO2);
    }
}
