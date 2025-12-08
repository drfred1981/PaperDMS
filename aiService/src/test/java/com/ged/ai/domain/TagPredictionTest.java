package com.ged.ai.domain;

import static com.ged.ai.domain.AutoTagJobTestSamples.*;
import static com.ged.ai.domain.TagPredictionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagPredictionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagPrediction.class);
        TagPrediction tagPrediction1 = getTagPredictionSample1();
        TagPrediction tagPrediction2 = new TagPrediction();
        assertThat(tagPrediction1).isNotEqualTo(tagPrediction2);

        tagPrediction2.setId(tagPrediction1.getId());
        assertThat(tagPrediction1).isEqualTo(tagPrediction2);

        tagPrediction2 = getTagPredictionSample2();
        assertThat(tagPrediction1).isNotEqualTo(tagPrediction2);
    }

    @Test
    void jobTest() {
        TagPrediction tagPrediction = getTagPredictionRandomSampleGenerator();
        AutoTagJob autoTagJobBack = getAutoTagJobRandomSampleGenerator();

        tagPrediction.setJob(autoTagJobBack);
        assertThat(tagPrediction.getJob()).isEqualTo(autoTagJobBack);

        tagPrediction.job(null);
        assertThat(tagPrediction.getJob()).isNull();
    }
}
