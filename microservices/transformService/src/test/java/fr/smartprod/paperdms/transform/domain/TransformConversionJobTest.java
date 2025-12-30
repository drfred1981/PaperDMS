package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.TransformConversionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformConversionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformConversionJob.class);
        TransformConversionJob transformConversionJob1 = getTransformConversionJobSample1();
        TransformConversionJob transformConversionJob2 = new TransformConversionJob();
        assertThat(transformConversionJob1).isNotEqualTo(transformConversionJob2);

        transformConversionJob2.setId(transformConversionJob1.getId());
        assertThat(transformConversionJob1).isEqualTo(transformConversionJob2);

        transformConversionJob2 = getTransformConversionJobSample2();
        assertThat(transformConversionJob1).isNotEqualTo(transformConversionJob2);
    }
}
