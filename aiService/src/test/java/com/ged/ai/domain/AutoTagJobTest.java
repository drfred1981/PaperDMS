package com.ged.ai.domain;

import static com.ged.ai.domain.AutoTagJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutoTagJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoTagJob.class);
        AutoTagJob autoTagJob1 = getAutoTagJobSample1();
        AutoTagJob autoTagJob2 = new AutoTagJob();
        assertThat(autoTagJob1).isNotEqualTo(autoTagJob2);

        autoTagJob2.setId(autoTagJob1.getId());
        assertThat(autoTagJob1).isEqualTo(autoTagJob2);

        autoTagJob2 = getAutoTagJobSample2();
        assertThat(autoTagJob1).isNotEqualTo(autoTagJob2);
    }
}
