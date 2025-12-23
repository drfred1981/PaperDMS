package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.PerformanceMetricTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerformanceMetricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceMetric.class);
        PerformanceMetric performanceMetric1 = getPerformanceMetricSample1();
        PerformanceMetric performanceMetric2 = new PerformanceMetric();
        assertThat(performanceMetric1).isNotEqualTo(performanceMetric2);

        performanceMetric2.setId(performanceMetric1.getId());
        assertThat(performanceMetric1).isEqualTo(performanceMetric2);

        performanceMetric2 = getPerformanceMetricSample2();
        assertThat(performanceMetric1).isNotEqualTo(performanceMetric2);
    }
}
