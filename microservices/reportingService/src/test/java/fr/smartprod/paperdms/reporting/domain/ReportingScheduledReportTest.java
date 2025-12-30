package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingExecutionTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingScheduledReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportingScheduledReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingScheduledReport.class);
        ReportingScheduledReport reportingScheduledReport1 = getReportingScheduledReportSample1();
        ReportingScheduledReport reportingScheduledReport2 = new ReportingScheduledReport();
        assertThat(reportingScheduledReport1).isNotEqualTo(reportingScheduledReport2);

        reportingScheduledReport2.setId(reportingScheduledReport1.getId());
        assertThat(reportingScheduledReport1).isEqualTo(reportingScheduledReport2);

        reportingScheduledReport2 = getReportingScheduledReportSample2();
        assertThat(reportingScheduledReport1).isNotEqualTo(reportingScheduledReport2);
    }

    @Test
    void reportsExecutionsTest() {
        ReportingScheduledReport reportingScheduledReport = getReportingScheduledReportRandomSampleGenerator();
        ReportingExecution reportingExecutionBack = getReportingExecutionRandomSampleGenerator();

        reportingScheduledReport.addReportsExecutions(reportingExecutionBack);
        assertThat(reportingScheduledReport.getReportsExecutions()).containsOnly(reportingExecutionBack);
        assertThat(reportingExecutionBack.getScheduledReport()).isEqualTo(reportingScheduledReport);

        reportingScheduledReport.removeReportsExecutions(reportingExecutionBack);
        assertThat(reportingScheduledReport.getReportsExecutions()).doesNotContain(reportingExecutionBack);
        assertThat(reportingExecutionBack.getScheduledReport()).isNull();

        reportingScheduledReport.reportsExecutions(new HashSet<>(Set.of(reportingExecutionBack)));
        assertThat(reportingScheduledReport.getReportsExecutions()).containsOnly(reportingExecutionBack);
        assertThat(reportingExecutionBack.getScheduledReport()).isEqualTo(reportingScheduledReport);

        reportingScheduledReport.setReportsExecutions(new HashSet<>());
        assertThat(reportingScheduledReport.getReportsExecutions()).doesNotContain(reportingExecutionBack);
        assertThat(reportingExecutionBack.getScheduledReport()).isNull();
    }
}
