package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.LanguageDetectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageDetectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageDetection.class);
        LanguageDetection languageDetection1 = getLanguageDetectionSample1();
        LanguageDetection languageDetection2 = new LanguageDetection();
        assertThat(languageDetection1).isNotEqualTo(languageDetection2);

        languageDetection2.setId(languageDetection1.getId());
        assertThat(languageDetection1).isEqualTo(languageDetection2);

        languageDetection2 = getLanguageDetectionSample2();
        assertThat(languageDetection1).isNotEqualTo(languageDetection2);
    }
}
