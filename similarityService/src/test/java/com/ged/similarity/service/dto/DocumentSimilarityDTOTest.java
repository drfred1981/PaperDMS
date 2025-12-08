package com.ged.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentSimilarityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentSimilarityDTO.class);
        DocumentSimilarityDTO documentSimilarityDTO1 = new DocumentSimilarityDTO();
        documentSimilarityDTO1.setId(1L);
        DocumentSimilarityDTO documentSimilarityDTO2 = new DocumentSimilarityDTO();
        assertThat(documentSimilarityDTO1).isNotEqualTo(documentSimilarityDTO2);
        documentSimilarityDTO2.setId(documentSimilarityDTO1.getId());
        assertThat(documentSimilarityDTO1).isEqualTo(documentSimilarityDTO2);
        documentSimilarityDTO2.setId(2L);
        assertThat(documentSimilarityDTO1).isNotEqualTo(documentSimilarityDTO2);
        documentSimilarityDTO1.setId(null);
        assertThat(documentSimilarityDTO1).isNotEqualTo(documentSimilarityDTO2);
    }
}
