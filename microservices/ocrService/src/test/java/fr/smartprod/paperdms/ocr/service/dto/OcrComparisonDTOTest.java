package fr.smartprod.paperdms.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrComparisonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrComparisonDTO.class);
        OcrComparisonDTO ocrComparisonDTO1 = new OcrComparisonDTO();
        ocrComparisonDTO1.setId(1L);
        OcrComparisonDTO ocrComparisonDTO2 = new OcrComparisonDTO();
        assertThat(ocrComparisonDTO1).isNotEqualTo(ocrComparisonDTO2);
        ocrComparisonDTO2.setId(ocrComparisonDTO1.getId());
        assertThat(ocrComparisonDTO1).isEqualTo(ocrComparisonDTO2);
        ocrComparisonDTO2.setId(2L);
        assertThat(ocrComparisonDTO1).isNotEqualTo(ocrComparisonDTO2);
        ocrComparisonDTO1.setId(null);
        assertThat(ocrComparisonDTO1).isNotEqualTo(ocrComparisonDTO2);
    }
}
