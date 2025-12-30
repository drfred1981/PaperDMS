package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AITypePredictionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AITypePredictionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AITypePrediction.class);
        AITypePrediction aITypePrediction1 = getAITypePredictionSample1();
        AITypePrediction aITypePrediction2 = new AITypePrediction();
        assertThat(aITypePrediction1).isNotEqualTo(aITypePrediction2);

        aITypePrediction2.setId(aITypePrediction1.getId());
        assertThat(aITypePrediction1).isEqualTo(aITypePrediction2);

        aITypePrediction2 = getAITypePredictionSample2();
        assertThat(aITypePrediction1).isNotEqualTo(aITypePrediction2);
    }

    @Test
    void jobTest() {
        AITypePrediction aITypePrediction = getAITypePredictionRandomSampleGenerator();
        AIAutoTagJob aIAutoTagJobBack = getAIAutoTagJobRandomSampleGenerator();

        aITypePrediction.setJob(aIAutoTagJobBack);
        assertThat(aITypePrediction.getJob()).isEqualTo(aIAutoTagJobBack);
        assertThat(aIAutoTagJobBack.getAITypePrediction()).isEqualTo(aITypePrediction);

        aITypePrediction.job(null);
        assertThat(aITypePrediction.getJob()).isNull();
        assertThat(aIAutoTagJobBack.getAITypePrediction()).isNull();
    }
}
