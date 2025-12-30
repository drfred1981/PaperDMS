package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.TransformWatermarkJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformWatermarkJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformWatermarkJob.class);
        TransformWatermarkJob transformWatermarkJob1 = getTransformWatermarkJobSample1();
        TransformWatermarkJob transformWatermarkJob2 = new TransformWatermarkJob();
        assertThat(transformWatermarkJob1).isNotEqualTo(transformWatermarkJob2);

        transformWatermarkJob2.setId(transformWatermarkJob1.getId());
        assertThat(transformWatermarkJob1).isEqualTo(transformWatermarkJob2);

        transformWatermarkJob2 = getTransformWatermarkJobSample2();
        assertThat(transformWatermarkJob1).isNotEqualTo(transformWatermarkJob2);
    }
}
