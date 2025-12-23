package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AutoTagJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
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
