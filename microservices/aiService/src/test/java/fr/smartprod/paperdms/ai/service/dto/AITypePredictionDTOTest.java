package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AITypePredictionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AITypePredictionDTO.class);
        AITypePredictionDTO aITypePredictionDTO1 = new AITypePredictionDTO();
        aITypePredictionDTO1.setId(1L);
        AITypePredictionDTO aITypePredictionDTO2 = new AITypePredictionDTO();
        assertThat(aITypePredictionDTO1).isNotEqualTo(aITypePredictionDTO2);
        aITypePredictionDTO2.setId(aITypePredictionDTO1.getId());
        assertThat(aITypePredictionDTO1).isEqualTo(aITypePredictionDTO2);
        aITypePredictionDTO2.setId(2L);
        assertThat(aITypePredictionDTO1).isNotEqualTo(aITypePredictionDTO2);
        aITypePredictionDTO1.setId(null);
        assertThat(aITypePredictionDTO1).isNotEqualTo(aITypePredictionDTO2);
    }
}
