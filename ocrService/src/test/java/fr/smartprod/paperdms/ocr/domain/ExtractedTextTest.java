package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.ExtractedTextTestSamples.*;
import static fr.smartprod.paperdms.ocr.domain.OcrJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtractedTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtractedText.class);
        ExtractedText extractedText1 = getExtractedTextSample1();
        ExtractedText extractedText2 = new ExtractedText();
        assertThat(extractedText1).isNotEqualTo(extractedText2);

        extractedText2.setId(extractedText1.getId());
        assertThat(extractedText1).isEqualTo(extractedText2);

        extractedText2 = getExtractedTextSample2();
        assertThat(extractedText1).isNotEqualTo(extractedText2);
    }

    @Test
    void jobTest() {
        ExtractedText extractedText = getExtractedTextRandomSampleGenerator();
        OcrJob ocrJobBack = getOcrJobRandomSampleGenerator();

        extractedText.setJob(ocrJobBack);
        assertThat(extractedText.getJob()).isEqualTo(ocrJobBack);

        extractedText.job(null);
        assertThat(extractedText.getJob()).isNull();
    }
}
