package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingSystemMetricTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingSystemMetricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingSystemMetric.class);
        ReportingSystemMetric reportingSystemMetric1 = getReportingSystemMetricSample1();
        ReportingSystemMetric reportingSystemMetric2 = new ReportingSystemMetric();
        assertThat(reportingSystemMetric1).isNotEqualTo(reportingSystemMetric2);

        reportingSystemMetric2.setId(reportingSystemMetric1.getId());
        assertThat(reportingSystemMetric1).isEqualTo(reportingSystemMetric2);

        reportingSystemMetric2 = getReportingSystemMetricSample2();
        assertThat(reportingSystemMetric1).isNotEqualTo(reportingSystemMetric2);
    }
}
