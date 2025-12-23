package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageDetectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageDetectionDTO.class);
        LanguageDetectionDTO languageDetectionDTO1 = new LanguageDetectionDTO();
        languageDetectionDTO1.setId(1L);
        LanguageDetectionDTO languageDetectionDTO2 = new LanguageDetectionDTO();
        assertThat(languageDetectionDTO1).isNotEqualTo(languageDetectionDTO2);
        languageDetectionDTO2.setId(languageDetectionDTO1.getId());
        assertThat(languageDetectionDTO1).isEqualTo(languageDetectionDTO2);
        languageDetectionDTO2.setId(2L);
        assertThat(languageDetectionDTO1).isNotEqualTo(languageDetectionDTO2);
        languageDetectionDTO1.setId(null);
        assertThat(languageDetectionDTO1).isNotEqualTo(languageDetectionDTO2);
    }
}
