package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.SystemMetricTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemMetricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemMetric.class);
        SystemMetric systemMetric1 = getSystemMetricSample1();
        SystemMetric systemMetric2 = new SystemMetric();
        assertThat(systemMetric1).isNotEqualTo(systemMetric2);

        systemMetric2.setId(systemMetric1.getId());
        assertThat(systemMetric1).isEqualTo(systemMetric2);

        systemMetric2 = getSystemMetricSample2();
        assertThat(systemMetric1).isNotEqualTo(systemMetric2);
    }
}
