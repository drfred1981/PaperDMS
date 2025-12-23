package fr.smartprod.paperdms.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrCacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrCacheDTO.class);
        OcrCacheDTO ocrCacheDTO1 = new OcrCacheDTO();
        ocrCacheDTO1.setId(1L);
        OcrCacheDTO ocrCacheDTO2 = new OcrCacheDTO();
        assertThat(ocrCacheDTO1).isNotEqualTo(ocrCacheDTO2);
        ocrCacheDTO2.setId(ocrCacheDTO1.getId());
        assertThat(ocrCacheDTO1).isEqualTo(ocrCacheDTO2);
        ocrCacheDTO2.setId(2L);
        assertThat(ocrCacheDTO1).isNotEqualTo(ocrCacheDTO2);
        ocrCacheDTO1.setId(null);
        assertThat(ocrCacheDTO1).isNotEqualTo(ocrCacheDTO2);
    }
}
