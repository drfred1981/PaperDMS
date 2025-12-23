package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportExecutionTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.ScheduledReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportExecution.class);
        ReportExecution reportExecution1 = getReportExecutionSample1();
        ReportExecution reportExecution2 = new ReportExecution();
        assertThat(reportExecution1).isNotEqualTo(reportExecution2);

        reportExecution2.setId(reportExecution1.getId());
        assertThat(reportExecution1).isEqualTo(reportExecution2);

        reportExecution2 = getReportExecutionSample2();
        assertThat(reportExecution1).isNotEqualTo(reportExecution2);
    }

    @Test
    void scheduledReportTest() {
        ReportExecution reportExecution = getReportExecutionRandomSampleGenerator();
        ScheduledReport scheduledReportBack = getScheduledReportRandomSampleGenerator();

        reportExecution.setScheduledReport(scheduledReportBack);
        assertThat(reportExecution.getScheduledReport()).isEqualTo(scheduledReportBack);

        reportExecution.scheduledReport(null);
        assertThat(reportExecution.getScheduledReport()).isNull();
    }
}
