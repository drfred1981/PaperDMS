package fr.smartprod.paperdms.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityDocumentFingerprintDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityDocumentFingerprintDTO.class);
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO1 = new SimilarityDocumentFingerprintDTO();
        similarityDocumentFingerprintDTO1.setId(1L);
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO2 = new SimilarityDocumentFingerprintDTO();
        assertThat(similarityDocumentFingerprintDTO1).isNotEqualTo(similarityDocumentFingerprintDTO2);
        similarityDocumentFingerprintDTO2.setId(similarityDocumentFingerprintDTO1.getId());
        assertThat(similarityDocumentFingerprintDTO1).isEqualTo(similarityDocumentFingerprintDTO2);
        similarityDocumentFingerprintDTO2.setId(2L);
        assertThat(similarityDocumentFingerprintDTO1).isNotEqualTo(similarityDocumentFingerprintDTO2);
        similarityDocumentFingerprintDTO1.setId(null);
        assertThat(similarityDocumentFingerprintDTO1).isNotEqualTo(similarityDocumentFingerprintDTO2);
    }
}
