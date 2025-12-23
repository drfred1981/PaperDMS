package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.WatermarkJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatermarkJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatermarkJob.class);
        WatermarkJob watermarkJob1 = getWatermarkJobSample1();
        WatermarkJob watermarkJob2 = new WatermarkJob();
        assertThat(watermarkJob1).isNotEqualTo(watermarkJob2);

        watermarkJob2.setId(watermarkJob1.getId());
        assertThat(watermarkJob1).isEqualTo(watermarkJob2);

        watermarkJob2 = getWatermarkJobSample2();
        assertThat(watermarkJob1).isNotEqualTo(watermarkJob2);
    }
}
