package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.OcrJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrJob.class);
        OcrJob ocrJob1 = getOcrJobSample1();
        OcrJob ocrJob2 = new OcrJob();
        assertThat(ocrJob1).isNotEqualTo(ocrJob2);

        ocrJob2.setId(ocrJob1.getId());
        assertThat(ocrJob1).isEqualTo(ocrJob2);

        ocrJob2 = getOcrJobSample2();
        assertThat(ocrJob1).isNotEqualTo(ocrJob2);
    }
}
