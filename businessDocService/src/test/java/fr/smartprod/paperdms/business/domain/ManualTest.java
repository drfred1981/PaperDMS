package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.ManualTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manual.class);
        Manual manual1 = getManualSample1();
        Manual manual2 = new Manual();
        assertThat(manual1).isNotEqualTo(manual2);

        manual2.setId(manual1.getId());
        assertThat(manual1).isEqualTo(manual2);

        manual2 = getManualSample2();
        assertThat(manual1).isNotEqualTo(manual2);
    }
}
