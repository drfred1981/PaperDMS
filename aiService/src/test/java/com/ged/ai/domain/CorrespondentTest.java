package com.ged.ai.domain;

import static com.ged.ai.domain.CorrespondentExtractionTestSamples.*;
import static com.ged.ai.domain.CorrespondentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrespondentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correspondent.class);
        Correspondent correspondent1 = getCorrespondentSample1();
        Correspondent correspondent2 = new Correspondent();
        assertThat(correspondent1).isNotEqualTo(correspondent2);

        correspondent2.setId(correspondent1.getId());
        assertThat(correspondent1).isEqualTo(correspondent2);

        correspondent2 = getCorrespondentSample2();
        assertThat(correspondent1).isNotEqualTo(correspondent2);
    }

    @Test
    void extractionTest() {
        Correspondent correspondent = getCorrespondentRandomSampleGenerator();
        CorrespondentExtraction correspondentExtractionBack = getCorrespondentExtractionRandomSampleGenerator();

        correspondent.setExtraction(correspondentExtractionBack);
        assertThat(correspondent.getExtraction()).isEqualTo(correspondentExtractionBack);

        correspondent.extraction(null);
        assertThat(correspondent.getExtraction()).isNull();
    }
}
