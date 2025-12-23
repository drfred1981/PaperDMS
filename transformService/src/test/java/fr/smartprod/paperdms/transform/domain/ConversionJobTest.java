package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.ConversionJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConversionJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConversionJob.class);
        ConversionJob conversionJob1 = getConversionJobSample1();
        ConversionJob conversionJob2 = new ConversionJob();
        assertThat(conversionJob1).isNotEqualTo(conversionJob2);

        conversionJob2.setId(conversionJob1.getId());
        assertThat(conversionJob1).isEqualTo(conversionJob2);

        conversionJob2 = getConversionJobSample2();
        assertThat(conversionJob1).isNotEqualTo(conversionJob2);
    }
}
