package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.OcrJobTestSamples.*;
import static fr.smartprod.paperdms.ocr.domain.OcrResultTestSamples.*;
import static fr.smartprod.paperdms.ocr.domain.OrcExtractedTextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void resultsTest() {
        OcrJob ocrJob = getOcrJobRandomSampleGenerator();
        OcrResult ocrResultBack = getOcrResultRandomSampleGenerator();

        ocrJob.addResults(ocrResultBack);
        assertThat(ocrJob.getResults()).containsOnly(ocrResultBack);
        assertThat(ocrResultBack.getJob()).isEqualTo(ocrJob);

        ocrJob.removeResults(ocrResultBack);
        assertThat(ocrJob.getResults()).doesNotContain(ocrResultBack);
        assertThat(ocrResultBack.getJob()).isNull();

        ocrJob.results(new HashSet<>(Set.of(ocrResultBack)));
        assertThat(ocrJob.getResults()).containsOnly(ocrResultBack);
        assertThat(ocrResultBack.getJob()).isEqualTo(ocrJob);

        ocrJob.setResults(new HashSet<>());
        assertThat(ocrJob.getResults()).doesNotContain(ocrResultBack);
        assertThat(ocrResultBack.getJob()).isNull();
    }

    @Test
    void extractedTest() {
        OcrJob ocrJob = getOcrJobRandomSampleGenerator();
        OrcExtractedText orcExtractedTextBack = getOrcExtractedTextRandomSampleGenerator();

        ocrJob.addExtracted(orcExtractedTextBack);
        assertThat(ocrJob.getExtracteds()).containsOnly(orcExtractedTextBack);
        assertThat(orcExtractedTextBack.getJob()).isEqualTo(ocrJob);

        ocrJob.removeExtracted(orcExtractedTextBack);
        assertThat(ocrJob.getExtracteds()).doesNotContain(orcExtractedTextBack);
        assertThat(orcExtractedTextBack.getJob()).isNull();

        ocrJob.extracteds(new HashSet<>(Set.of(orcExtractedTextBack)));
        assertThat(ocrJob.getExtracteds()).containsOnly(orcExtractedTextBack);
        assertThat(orcExtractedTextBack.getJob()).isEqualTo(ocrJob);

        ocrJob.setExtracteds(new HashSet<>());
        assertThat(ocrJob.getExtracteds()).doesNotContain(orcExtractedTextBack);
        assertThat(orcExtractedTextBack.getJob()).isNull();
    }
}
