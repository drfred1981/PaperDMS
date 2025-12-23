package fr.smartprod.paperdms.ocr.domain;

import static fr.smartprod.paperdms.ocr.domain.OcrCacheTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrCacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrCache.class);
        OcrCache ocrCache1 = getOcrCacheSample1();
        OcrCache ocrCache2 = new OcrCache();
        assertThat(ocrCache1).isNotEqualTo(ocrCache2);

        ocrCache2.setId(ocrCache1.getId());
        assertThat(ocrCache1).isEqualTo(ocrCache2);

        ocrCache2 = getOcrCacheSample2();
        assertThat(ocrCache1).isNotEqualTo(ocrCache2);
    }
}
