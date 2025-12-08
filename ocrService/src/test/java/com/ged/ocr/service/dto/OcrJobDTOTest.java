package com.ged.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrJobDTO.class);
        OcrJobDTO ocrJobDTO1 = new OcrJobDTO();
        ocrJobDTO1.setId(1L);
        OcrJobDTO ocrJobDTO2 = new OcrJobDTO();
        assertThat(ocrJobDTO1).isNotEqualTo(ocrJobDTO2);
        ocrJobDTO2.setId(ocrJobDTO1.getId());
        assertThat(ocrJobDTO1).isEqualTo(ocrJobDTO2);
        ocrJobDTO2.setId(2L);
        assertThat(ocrJobDTO1).isNotEqualTo(ocrJobDTO2);
        ocrJobDTO1.setId(null);
        assertThat(ocrJobDTO1).isNotEqualTo(ocrJobDTO2);
    }
}
