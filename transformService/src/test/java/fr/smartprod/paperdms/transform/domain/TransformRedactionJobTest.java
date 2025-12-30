package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.TransformRedactionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformRedactionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformRedactionJob.class);
        TransformRedactionJob transformRedactionJob1 = getTransformRedactionJobSample1();
        TransformRedactionJob transformRedactionJob2 = new TransformRedactionJob();
        assertThat(transformRedactionJob1).isNotEqualTo(transformRedactionJob2);

        transformRedactionJob2.setId(transformRedactionJob1.getId());
        assertThat(transformRedactionJob1).isEqualTo(transformRedactionJob2);

        transformRedactionJob2 = getTransformRedactionJobSample2();
        assertThat(transformRedactionJob1).isNotEqualTo(transformRedactionJob2);
    }
}
