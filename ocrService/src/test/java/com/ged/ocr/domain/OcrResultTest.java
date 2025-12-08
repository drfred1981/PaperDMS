package com.ged.ocr.domain;

import static com.ged.ocr.domain.OcrJobTestSamples.*;
import static com.ged.ocr.domain.OcrResultTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrResult.class);
        OcrResult ocrResult1 = getOcrResultSample1();
        OcrResult ocrResult2 = new OcrResult();
        assertThat(ocrResult1).isNotEqualTo(ocrResult2);

        ocrResult2.setId(ocrResult1.getId());
        assertThat(ocrResult1).isEqualTo(ocrResult2);

        ocrResult2 = getOcrResultSample2();
        assertThat(ocrResult1).isNotEqualTo(ocrResult2);
    }

    @Test
    void jobTest() {
        OcrResult ocrResult = getOcrResultRandomSampleGenerator();
        OcrJob ocrJobBack = getOcrJobRandomSampleGenerator();

        ocrResult.setJob(ocrJobBack);
        assertThat(ocrResult.getJob()).isEqualTo(ocrJobBack);

        ocrResult.job(null);
        assertThat(ocrResult.getJob()).isNull();
    }
}
