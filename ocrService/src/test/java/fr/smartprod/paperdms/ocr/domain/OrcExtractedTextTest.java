package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.OcrJobTestSamples.*;
import static fr.smartprod.paperdms.ocr.domain.OrcExtractedTextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrcExtractedTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrcExtractedText.class);
        OrcExtractedText orcExtractedText1 = getOrcExtractedTextSample1();
        OrcExtractedText orcExtractedText2 = new OrcExtractedText();
        assertThat(orcExtractedText1).isNotEqualTo(orcExtractedText2);

        orcExtractedText2.setId(orcExtractedText1.getId());
        assertThat(orcExtractedText1).isEqualTo(orcExtractedText2);

        orcExtractedText2 = getOrcExtractedTextSample2();
        assertThat(orcExtractedText1).isNotEqualTo(orcExtractedText2);
    }

    @Test
    void jobTest() {
        OrcExtractedText orcExtractedText = getOrcExtractedTextRandomSampleGenerator();
        OcrJob ocrJobBack = getOcrJobRandomSampleGenerator();

        orcExtractedText.setJob(ocrJobBack);
        assertThat(orcExtractedText.getJob()).isEqualTo(ocrJobBack);

        orcExtractedText.job(null);
        assertThat(orcExtractedText.getJob()).isNull();
    }
}
