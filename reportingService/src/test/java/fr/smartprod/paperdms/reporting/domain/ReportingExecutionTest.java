package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingExecutionTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingScheduledReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingExecution.class);
        ReportingExecution reportingExecution1 = getReportingExecutionSample1();
        ReportingExecution reportingExecution2 = new ReportingExecution();
        assertThat(reportingExecution1).isNotEqualTo(reportingExecution2);

        reportingExecution2.setId(reportingExecution1.getId());
        assertThat(reportingExecution1).isEqualTo(reportingExecution2);

        reportingExecution2 = getReportingExecutionSample2();
        assertThat(reportingExecution1).isNotEqualTo(reportingExecution2);
    }

    @Test
    void scheduledReportTest() {
        ReportingExecution reportingExecution = getReportingExecutionRandomSampleGenerator();
        ReportingScheduledReport reportingScheduledReportBack = getReportingScheduledReportRandomSampleGenerator();

        reportingExecution.setScheduledReport(reportingScheduledReportBack);
        assertThat(reportingExecution.getScheduledReport()).isEqualTo(reportingScheduledReportBack);

        reportingExecution.scheduledReport(null);
        assertThat(reportingExecution.getScheduledReport()).isNull();
    }
}
