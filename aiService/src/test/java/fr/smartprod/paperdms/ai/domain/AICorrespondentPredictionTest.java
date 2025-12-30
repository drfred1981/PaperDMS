package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AICorrespondentPredictionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AICorrespondentPredictionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AICorrespondentPrediction.class);
        AICorrespondentPrediction aICorrespondentPrediction1 = getAICorrespondentPredictionSample1();
        AICorrespondentPrediction aICorrespondentPrediction2 = new AICorrespondentPrediction();
        assertThat(aICorrespondentPrediction1).isNotEqualTo(aICorrespondentPrediction2);

        aICorrespondentPrediction2.setId(aICorrespondentPrediction1.getId());
        assertThat(aICorrespondentPrediction1).isEqualTo(aICorrespondentPrediction2);

        aICorrespondentPrediction2 = getAICorrespondentPredictionSample2();
        assertThat(aICorrespondentPrediction1).isNotEqualTo(aICorrespondentPrediction2);
    }

    @Test
    void jobTest() {
        AICorrespondentPrediction aICorrespondentPrediction = getAICorrespondentPredictionRandomSampleGenerator();
        AIAutoTagJob aIAutoTagJobBack = getAIAutoTagJobRandomSampleGenerator();

        aICorrespondentPrediction.setJob(aIAutoTagJobBack);
        assertThat(aICorrespondentPrediction.getJob()).isEqualTo(aIAutoTagJobBack);

        aICorrespondentPrediction.job(null);
        assertThat(aICorrespondentPrediction.getJob()).isNull();
    }
}
