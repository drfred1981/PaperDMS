package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.CorrespondentExtractionTestSamples.*;
import static fr.smartprod.paperdms.ai.domain.CorrespondentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
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
