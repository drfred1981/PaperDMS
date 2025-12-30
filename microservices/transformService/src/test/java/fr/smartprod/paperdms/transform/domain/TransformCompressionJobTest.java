package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.TransformCompressionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformCompressionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformCompressionJob.class);
        TransformCompressionJob transformCompressionJob1 = getTransformCompressionJobSample1();
        TransformCompressionJob transformCompressionJob2 = new TransformCompressionJob();
        assertThat(transformCompressionJob1).isNotEqualTo(transformCompressionJob2);

        transformCompressionJob2.setId(transformCompressionJob1.getId());
        assertThat(transformCompressionJob1).isEqualTo(transformCompressionJob2);

        transformCompressionJob2 = getTransformCompressionJobSample2();
        assertThat(transformCompressionJob1).isNotEqualTo(transformCompressionJob2);
    }
}
