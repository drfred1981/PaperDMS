package com.ged.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityClusterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityClusterDTO.class);
        SimilarityClusterDTO similarityClusterDTO1 = new SimilarityClusterDTO();
        similarityClusterDTO1.setId(1L);
        SimilarityClusterDTO similarityClusterDTO2 = new SimilarityClusterDTO();
        assertThat(similarityClusterDTO1).isNotEqualTo(similarityClusterDTO2);
        similarityClusterDTO2.setId(similarityClusterDTO1.getId());
        assertThat(similarityClusterDTO1).isEqualTo(similarityClusterDTO2);
        similarityClusterDTO2.setId(2L);
        assertThat(similarityClusterDTO1).isNotEqualTo(similarityClusterDTO2);
        similarityClusterDTO1.setId(null);
        assertThat(similarityClusterDTO1).isNotEqualTo(similarityClusterDTO2);
    }
}
