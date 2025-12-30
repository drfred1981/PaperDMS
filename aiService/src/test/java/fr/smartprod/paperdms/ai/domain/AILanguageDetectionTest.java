package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AILanguageDetectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AILanguageDetectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AILanguageDetection.class);
        AILanguageDetection aILanguageDetection1 = getAILanguageDetectionSample1();
        AILanguageDetection aILanguageDetection2 = new AILanguageDetection();
        assertThat(aILanguageDetection1).isNotEqualTo(aILanguageDetection2);

        aILanguageDetection2.setId(aILanguageDetection1.getId());
        assertThat(aILanguageDetection1).isEqualTo(aILanguageDetection2);

        aILanguageDetection2 = getAILanguageDetectionSample2();
        assertThat(aILanguageDetection1).isNotEqualTo(aILanguageDetection2);
    }

    @Test
    void jobTest() {
        AILanguageDetection aILanguageDetection = getAILanguageDetectionRandomSampleGenerator();
        AIAutoTagJob aIAutoTagJobBack = getAIAutoTagJobRandomSampleGenerator();

        aILanguageDetection.setJob(aIAutoTagJobBack);
        assertThat(aILanguageDetection.getJob()).isEqualTo(aIAutoTagJobBack);
        assertThat(aIAutoTagJobBack.getLanguagePrediction()).isEqualTo(aILanguageDetection);

        aILanguageDetection.job(null);
        assertThat(aILanguageDetection.getJob()).isNull();
        assertThat(aIAutoTagJobBack.getLanguagePrediction()).isNull();
    }
}
