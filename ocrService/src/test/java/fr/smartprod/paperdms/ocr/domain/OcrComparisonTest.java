package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.OcrComparisonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrComparisonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrComparison.class);
        OcrComparison ocrComparison1 = getOcrComparisonSample1();
        OcrComparison ocrComparison2 = new OcrComparison();
        assertThat(ocrComparison1).isNotEqualTo(ocrComparison2);

        ocrComparison2.setId(ocrComparison1.getId());
        assertThat(ocrComparison1).isEqualTo(ocrComparison2);

        ocrComparison2 = getOcrComparisonSample2();
        assertThat(ocrComparison1).isNotEqualTo(ocrComparison2);
    }
}
