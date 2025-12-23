package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.RedactionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedactionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedactionJob.class);
        RedactionJob redactionJob1 = getRedactionJobSample1();
        RedactionJob redactionJob2 = new RedactionJob();
        assertThat(redactionJob1).isNotEqualTo(redactionJob2);

        redactionJob2.setId(redactionJob1.getId());
        assertThat(redactionJob1).isEqualTo(redactionJob2);

        redactionJob2 = getRedactionJobSample2();
        assertThat(redactionJob1).isNotEqualTo(redactionJob2);
    }
}
