package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.TransformMergeJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformMergeJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformMergeJob.class);
        TransformMergeJob transformMergeJob1 = getTransformMergeJobSample1();
        TransformMergeJob transformMergeJob2 = new TransformMergeJob();
        assertThat(transformMergeJob1).isNotEqualTo(transformMergeJob2);

        transformMergeJob2.setId(transformMergeJob1.getId());
        assertThat(transformMergeJob1).isEqualTo(transformMergeJob2);

        transformMergeJob2 = getTransformMergeJobSample2();
        assertThat(transformMergeJob1).isNotEqualTo(transformMergeJob2);
    }
}
