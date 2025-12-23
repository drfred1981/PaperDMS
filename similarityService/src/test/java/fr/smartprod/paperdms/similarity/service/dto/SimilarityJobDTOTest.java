package fr.smartprod.paperdms.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityJobDTO.class);
        SimilarityJobDTO similarityJobDTO1 = new SimilarityJobDTO();
        similarityJobDTO1.setId(1L);
        SimilarityJobDTO similarityJobDTO2 = new SimilarityJobDTO();
        assertThat(similarityJobDTO1).isNotEqualTo(similarityJobDTO2);
        similarityJobDTO2.setId(similarityJobDTO1.getId());
        assertThat(similarityJobDTO1).isEqualTo(similarityJobDTO2);
        similarityJobDTO2.setId(2L);
        assertThat(similarityJobDTO1).isNotEqualTo(similarityJobDTO2);
        similarityJobDTO1.setId(null);
        assertThat(similarityJobDTO1).isNotEqualTo(similarityJobDTO2);
    }
}
