package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AICorrespondentPredictionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AICorrespondentPredictionDTO.class);
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO1 = new AICorrespondentPredictionDTO();
        aICorrespondentPredictionDTO1.setId(1L);
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO2 = new AICorrespondentPredictionDTO();
        assertThat(aICorrespondentPredictionDTO1).isNotEqualTo(aICorrespondentPredictionDTO2);
        aICorrespondentPredictionDTO2.setId(aICorrespondentPredictionDTO1.getId());
        assertThat(aICorrespondentPredictionDTO1).isEqualTo(aICorrespondentPredictionDTO2);
        aICorrespondentPredictionDTO2.setId(2L);
        assertThat(aICorrespondentPredictionDTO1).isNotEqualTo(aICorrespondentPredictionDTO2);
        aICorrespondentPredictionDTO1.setId(null);
        assertThat(aICorrespondentPredictionDTO1).isNotEqualTo(aICorrespondentPredictionDTO2);
    }
}
