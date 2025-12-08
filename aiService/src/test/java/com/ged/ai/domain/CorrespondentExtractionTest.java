package com.ged.ai.domain;

import static com.ged.ai.domain.CorrespondentExtractionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrespondentExtractionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrespondentExtraction.class);
        CorrespondentExtraction correspondentExtraction1 = getCorrespondentExtractionSample1();
        CorrespondentExtraction correspondentExtraction2 = new CorrespondentExtraction();
        assertThat(correspondentExtraction1).isNotEqualTo(correspondentExtraction2);

        correspondentExtraction2.setId(correspondentExtraction1.getId());
        assertThat(correspondentExtraction1).isEqualTo(correspondentExtraction2);

        correspondentExtraction2 = getCorrespondentExtractionSample2();
        assertThat(correspondentExtraction1).isNotEqualTo(correspondentExtraction2);
    }
}
