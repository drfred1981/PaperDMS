package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.MergeJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MergeJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MergeJob.class);
        MergeJob mergeJob1 = getMergeJobSample1();
        MergeJob mergeJob2 = new MergeJob();
        assertThat(mergeJob1).isNotEqualTo(mergeJob2);

        mergeJob2.setId(mergeJob1.getId());
        assertThat(mergeJob1).isEqualTo(mergeJob2);

        mergeJob2 = getMergeJobSample2();
        assertThat(mergeJob1).isNotEqualTo(mergeJob2);
    }
}
