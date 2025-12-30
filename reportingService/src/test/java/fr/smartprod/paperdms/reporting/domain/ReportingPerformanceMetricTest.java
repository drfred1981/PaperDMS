package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetricTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingPerformanceMetricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingPerformanceMetric.class);
        ReportingPerformanceMetric reportingPerformanceMetric1 = getReportingPerformanceMetricSample1();
        ReportingPerformanceMetric reportingPerformanceMetric2 = new ReportingPerformanceMetric();
        assertThat(reportingPerformanceMetric1).isNotEqualTo(reportingPerformanceMetric2);

        reportingPerformanceMetric2.setId(reportingPerformanceMetric1.getId());
        assertThat(reportingPerformanceMetric1).isEqualTo(reportingPerformanceMetric2);

        reportingPerformanceMetric2 = getReportingPerformanceMetricSample2();
        assertThat(reportingPerformanceMetric1).isNotEqualTo(reportingPerformanceMetric2);
    }
}
