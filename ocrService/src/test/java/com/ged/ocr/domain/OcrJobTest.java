package com.ged.ocr.domain;

import static com.ged.ocr.domain.OcrJobTestSamples.*;
import static com.ged.ocr.domain.TikaConfigurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ocr.web.rest.TestUtil;
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

    @Test
    void tikaConfigTest() {
        OcrJob ocrJob = getOcrJobRandomSampleGenerator();
        TikaConfiguration tikaConfigurationBack = getTikaConfigurationRandomSampleGenerator();

        ocrJob.setTikaConfig(tikaConfigurationBack);
        assertThat(ocrJob.getTikaConfig()).isEqualTo(tikaConfigurationBack);

        ocrJob.tikaConfig(null);
        assertThat(ocrJob.getTikaConfig()).isNull();
    }
}
