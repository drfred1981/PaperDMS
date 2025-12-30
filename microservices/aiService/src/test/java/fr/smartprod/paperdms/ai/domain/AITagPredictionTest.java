package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.AITagPredictionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AITagPredictionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AITagPrediction.class);
        AITagPrediction aITagPrediction1 = getAITagPredictionSample1();
        AITagPrediction aITagPrediction2 = new AITagPrediction();
        assertThat(aITagPrediction1).isNotEqualTo(aITagPrediction2);

        aITagPrediction2.setId(aITagPrediction1.getId());
        assertThat(aITagPrediction1).isEqualTo(aITagPrediction2);

        aITagPrediction2 = getAITagPredictionSample2();
        assertThat(aITagPrediction1).isNotEqualTo(aITagPrediction2);
    }

    @Test
    void jobTest() {
        AITagPrediction aITagPrediction = getAITagPredictionRandomSampleGenerator();
        AIAutoTagJob aIAutoTagJobBack = getAIAutoTagJobRandomSampleGenerator();

        aITagPrediction.setJob(aIAutoTagJobBack);
        assertThat(aITagPrediction.getJob()).isEqualTo(aIAutoTagJobBack);

        aITagPrediction.job(null);
        assertThat(aITagPrediction.getJob()).isNull();
    }
}
