package com.ged.ocr.domain;

import static com.ged.ocr.domain.TikaConfigurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TikaConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TikaConfiguration.class);
        TikaConfiguration tikaConfiguration1 = getTikaConfigurationSample1();
        TikaConfiguration tikaConfiguration2 = new TikaConfiguration();
        assertThat(tikaConfiguration1).isNotEqualTo(tikaConfiguration2);

        tikaConfiguration2.setId(tikaConfiguration1.getId());
        assertThat(tikaConfiguration1).isEqualTo(tikaConfiguration2);

        tikaConfiguration2 = getTikaConfigurationSample2();
        assertThat(tikaConfiguration1).isNotEqualTo(tikaConfiguration2);
    }
}
