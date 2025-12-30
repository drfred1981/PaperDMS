package fr.smartprod.paperdms.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrResultDTO.class);
        OcrResultDTO ocrResultDTO1 = new OcrResultDTO();
        ocrResultDTO1.setId(1L);
        OcrResultDTO ocrResultDTO2 = new OcrResultDTO();
        assertThat(ocrResultDTO1).isNotEqualTo(ocrResultDTO2);
        ocrResultDTO2.setId(ocrResultDTO1.getId());
        assertThat(ocrResultDTO1).isEqualTo(ocrResultDTO2);
        ocrResultDTO2.setId(2L);
        assertThat(ocrResultDTO1).isNotEqualTo(ocrResultDTO2);
        ocrResultDTO1.setId(null);
        assertThat(ocrResultDTO1).isNotEqualTo(ocrResultDTO2);
    }
}
