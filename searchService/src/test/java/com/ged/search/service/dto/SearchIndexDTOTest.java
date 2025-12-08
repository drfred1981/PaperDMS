package com.ged.search.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.search.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchIndexDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchIndexDTO.class);
        SearchIndexDTO searchIndexDTO1 = new SearchIndexDTO();
        searchIndexDTO1.setId(1L);
        SearchIndexDTO searchIndexDTO2 = new SearchIndexDTO();
        assertThat(searchIndexDTO1).isNotEqualTo(searchIndexDTO2);
        searchIndexDTO2.setId(searchIndexDTO1.getId());
        assertThat(searchIndexDTO1).isEqualTo(searchIndexDTO2);
        searchIndexDTO2.setId(2L);
        assertThat(searchIndexDTO1).isNotEqualTo(searchIndexDTO2);
        searchIndexDTO1.setId(null);
        assertThat(searchIndexDTO1).isNotEqualTo(searchIndexDTO2);
    }
}
