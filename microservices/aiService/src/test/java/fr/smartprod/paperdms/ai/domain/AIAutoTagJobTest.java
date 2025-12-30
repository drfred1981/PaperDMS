package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AICorrespondentPredictionTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AILanguageDetectionTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AITagPredictionTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AITypePredictionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AIAutoTagJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIAutoTagJob.class);
        AIAutoTagJob aIAutoTagJob1 = getAIAutoTagJobSample1();
        AIAutoTagJob aIAutoTagJob2 = new AIAutoTagJob();
        assertThat(aIAutoTagJob1).isNotEqualTo(aIAutoTagJob2);

        aIAutoTagJob2.setId(aIAutoTagJob1.getId());
        assertThat(aIAutoTagJob1).isEqualTo(aIAutoTagJob2);

        aIAutoTagJob2 = getAIAutoTagJobSample2();
        assertThat(aIAutoTagJob1).isNotEqualTo(aIAutoTagJob2);
    }

    @Test
    void aITypePredictionTest() {
        AIAutoTagJob aIAutoTagJob = getAIAutoTagJobRandomSampleGenerator();
        AITypePrediction aITypePredictionBack = getAITypePredictionRandomSampleGenerator();

        aIAutoTagJob.setAITypePrediction(aITypePredictionBack);
        assertThat(aIAutoTagJob.getAITypePrediction()).isEqualTo(aITypePredictionBack);

        aIAutoTagJob.aITypePrediction(null);
        assertThat(aIAutoTagJob.getAITypePrediction()).isNull();
    }

    @Test
    void languagePredictionTest() {
        AIAutoTagJob aIAutoTagJob = getAIAutoTagJobRandomSampleGenerator();
        AILanguageDetection aILanguageDetectionBack = getAILanguageDetectionRandomSampleGenerator();

        aIAutoTagJob.setLanguagePrediction(aILanguageDetectionBack);
        assertThat(aIAutoTagJob.getLanguagePrediction()).isEqualTo(aILanguageDetectionBack);

        aIAutoTagJob.languagePrediction(null);
        assertThat(aIAutoTagJob.getLanguagePrediction()).isNull();
    }

    @Test
    void aITagPredictionsTest() {
        AIAutoTagJob aIAutoTagJob = getAIAutoTagJobRandomSampleGenerator();
        AITagPrediction aITagPredictionBack = getAITagPredictionRandomSampleGenerator();

        aIAutoTagJob.addAITagPredictions(aITagPredictionBack);
        assertThat(aIAutoTagJob.getAITagPredictions()).containsOnly(aITagPredictionBack);
        assertThat(aITagPredictionBack.getJob()).isEqualTo(aIAutoTagJob);

        aIAutoTagJob.removeAITagPredictions(aITagPredictionBack);
        assertThat(aIAutoTagJob.getAITagPredictions()).doesNotContain(aITagPredictionBack);
        assertThat(aITagPredictionBack.getJob()).isNull();

        aIAutoTagJob.aITagPredictions(new HashSet<>(Set.of(aITagPredictionBack)));
        assertThat(aIAutoTagJob.getAITagPredictions()).containsOnly(aITagPredictionBack);
        assertThat(aITagPredictionBack.getJob()).isEqualTo(aIAutoTagJob);

        aIAutoTagJob.setAITagPredictions(new HashSet<>());
        assertThat(aIAutoTagJob.getAITagPredictions()).doesNotContain(aITagPredictionBack);
        assertThat(aITagPredictionBack.getJob()).isNull();
    }

    @Test
    void aICorrespondentPredictionsTest() {
        AIAutoTagJob aIAutoTagJob = getAIAutoTagJobRandomSampleGenerator();
        AICorrespondentPrediction aICorrespondentPredictionBack = getAICorrespondentPredictionRandomSampleGenerator();

        aIAutoTagJob.addAICorrespondentPredictions(aICorrespondentPredictionBack);
        assertThat(aIAutoTagJob.getAICorrespondentPredictions()).containsOnly(aICorrespondentPredictionBack);
        assertThat(aICorrespondentPredictionBack.getJob()).isEqualTo(aIAutoTagJob);

        aIAutoTagJob.removeAICorrespondentPredictions(aICorrespondentPredictionBack);
        assertThat(aIAutoTagJob.getAICorrespondentPredictions()).doesNotContain(aICorrespondentPredictionBack);
        assertThat(aICorrespondentPredictionBack.getJob()).isNull();

        aIAutoTagJob.aICorrespondentPredictions(new HashSet<>(Set.of(aICorrespondentPredictionBack)));
        assertThat(aIAutoTagJob.getAICorrespondentPredictions()).containsOnly(aICorrespondentPredictionBack);
        assertThat(aICorrespondentPredictionBack.getJob()).isEqualTo(aIAutoTagJob);

        aIAutoTagJob.setAICorrespondentPredictions(new HashSet<>());
        assertThat(aIAutoTagJob.getAICorrespondentPredictions()).doesNotContain(aICorrespondentPredictionBack);
        assertThat(aICorrespondentPredictionBack.getJob()).isNull();
    }
}
