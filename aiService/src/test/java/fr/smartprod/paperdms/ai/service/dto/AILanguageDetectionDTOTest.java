package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AILanguageDetectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AILanguageDetectionDTO.class);
        AILanguageDetectionDTO aILanguageDetectionDTO1 = new AILanguageDetectionDTO();
        aILanguageDetectionDTO1.setId(1L);
        AILanguageDetectionDTO aILanguageDetectionDTO2 = new AILanguageDetectionDTO();
        assertThat(aILanguageDetectionDTO1).isNotEqualTo(aILanguageDetectionDTO2);
        aILanguageDetectionDTO2.setId(aILanguageDetectionDTO1.getId());
        assertThat(aILanguageDetectionDTO1).isEqualTo(aILanguageDetectionDTO2);
        aILanguageDetectionDTO2.setId(2L);
        assertThat(aILanguageDetectionDTO1).isNotEqualTo(aILanguageDetectionDTO2);
        aILanguageDetectionDTO1.setId(null);
        assertThat(aILanguageDetectionDTO1).isNotEqualTo(aILanguageDetectionDTO2);
    }
}
