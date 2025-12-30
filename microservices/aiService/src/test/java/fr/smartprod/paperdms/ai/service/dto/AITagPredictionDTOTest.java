package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AITagPredictionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AITagPredictionDTO.class);
        AITagPredictionDTO aITagPredictionDTO1 = new AITagPredictionDTO();
        aITagPredictionDTO1.setId(1L);
        AITagPredictionDTO aITagPredictionDTO2 = new AITagPredictionDTO();
        assertThat(aITagPredictionDTO1).isNotEqualTo(aITagPredictionDTO2);
        aITagPredictionDTO2.setId(aITagPredictionDTO1.getId());
        assertThat(aITagPredictionDTO1).isEqualTo(aITagPredictionDTO2);
        aITagPredictionDTO2.setId(2L);
        assertThat(aITagPredictionDTO1).isNotEqualTo(aITagPredictionDTO2);
        aITagPredictionDTO1.setId(null);
        assertThat(aITagPredictionDTO1).isNotEqualTo(aITagPredictionDTO2);
    }
}
