package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ScheduledReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduledReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledReport.class);
        ScheduledReport scheduledReport1 = getScheduledReportSample1();
        ScheduledReport scheduledReport2 = new ScheduledReport();
        assertThat(scheduledReport1).isNotEqualTo(scheduledReport2);

        scheduledReport2.setId(scheduledReport1.getId());
        assertThat(scheduledReport1).isEqualTo(scheduledReport2);

        scheduledReport2 = getScheduledReportSample2();
        assertThat(scheduledReport1).isNotEqualTo(scheduledReport2);
    }
}
