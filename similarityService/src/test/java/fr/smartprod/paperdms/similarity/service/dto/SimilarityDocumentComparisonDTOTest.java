package fr.smartprod.paperdms.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityDocumentComparisonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityDocumentComparisonDTO.class);
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO1 = new SimilarityDocumentComparisonDTO();
        similarityDocumentComparisonDTO1.setId(1L);
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO2 = new SimilarityDocumentComparisonDTO();
        assertThat(similarityDocumentComparisonDTO1).isNotEqualTo(similarityDocumentComparisonDTO2);
        similarityDocumentComparisonDTO2.setId(similarityDocumentComparisonDTO1.getId());
        assertThat(similarityDocumentComparisonDTO1).isEqualTo(similarityDocumentComparisonDTO2);
        similarityDocumentComparisonDTO2.setId(2L);
        assertThat(similarityDocumentComparisonDTO1).isNotEqualTo(similarityDocumentComparisonDTO2);
        similarityDocumentComparisonDTO1.setId(null);
        assertThat(similarityDocumentComparisonDTO1).isNotEqualTo(similarityDocumentComparisonDTO2);
    }
}
