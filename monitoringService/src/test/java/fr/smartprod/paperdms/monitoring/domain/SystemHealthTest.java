package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.SystemHealthTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemHealthTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemHealth.class);
        SystemHealth systemHealth1 = getSystemHealthSample1();
        SystemHealth systemHealth2 = new SystemHealth();
        assertThat(systemHealth1).isNotEqualTo(systemHealth2);

        systemHealth2.setId(systemHealth1.getId());
        assertThat(systemHealth1).isEqualTo(systemHealth2);

        systemHealth2 = getSystemHealthSample2();
        assertThat(systemHealth1).isNotEqualTo(systemHealth2);
    }
}
