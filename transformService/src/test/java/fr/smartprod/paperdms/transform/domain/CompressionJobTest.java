package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.CompressionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompressionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompressionJob.class);
        CompressionJob compressionJob1 = getCompressionJobSample1();
        CompressionJob compressionJob2 = new CompressionJob();
        assertThat(compressionJob1).isNotEqualTo(compressionJob2);

        compressionJob2.setId(compressionJob1.getId());
        assertThat(compressionJob1).isEqualTo(compressionJob2);

        compressionJob2 = getCompressionJobSample2();
        assertThat(compressionJob1).isNotEqualTo(compressionJob2);
    }
}
