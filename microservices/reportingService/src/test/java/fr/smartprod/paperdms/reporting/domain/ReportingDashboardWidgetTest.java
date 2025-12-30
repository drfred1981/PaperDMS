package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingDashboardWidgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboardWidget.class);
        ReportingDashboardWidget reportingDashboardWidget1 = getReportingDashboardWidgetSample1();
        ReportingDashboardWidget reportingDashboardWidget2 = new ReportingDashboardWidget();
        assertThat(reportingDashboardWidget1).isNotEqualTo(reportingDashboardWidget2);

        reportingDashboardWidget2.setId(reportingDashboardWidget1.getId());
        assertThat(reportingDashboardWidget1).isEqualTo(reportingDashboardWidget2);

        reportingDashboardWidget2 = getReportingDashboardWidgetSample2();
        assertThat(reportingDashboardWidget1).isNotEqualTo(reportingDashboardWidget2);
    }

    @Test
    void dashboarTest() {
        ReportingDashboardWidget reportingDashboardWidget = getReportingDashboardWidgetRandomSampleGenerator();
        ReportingDashboard reportingDashboardBack = getReportingDashboardRandomSampleGenerator();

        reportingDashboardWidget.setDashboar(reportingDashboardBack);
        assertThat(reportingDashboardWidget.getDashboar()).isEqualTo(reportingDashboardBack);

        reportingDashboardWidget.dashboar(null);
        assertThat(reportingDashboardWidget.getDashboar()).isNull();
    }
}
