package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportingDashboardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboard.class);
        ReportingDashboard reportingDashboard1 = getReportingDashboardSample1();
        ReportingDashboard reportingDashboard2 = new ReportingDashboard();
        assertThat(reportingDashboard1).isNotEqualTo(reportingDashboard2);

        reportingDashboard2.setId(reportingDashboard1.getId());
        assertThat(reportingDashboard1).isEqualTo(reportingDashboard2);

        reportingDashboard2 = getReportingDashboardSample2();
        assertThat(reportingDashboard1).isNotEqualTo(reportingDashboard2);
    }

    @Test
    void widgetsTest() {
        ReportingDashboard reportingDashboard = getReportingDashboardRandomSampleGenerator();
        ReportingDashboardWidget reportingDashboardWidgetBack = getReportingDashboardWidgetRandomSampleGenerator();

        reportingDashboard.addWidgets(reportingDashboardWidgetBack);
        assertThat(reportingDashboard.getWidgets()).containsOnly(reportingDashboardWidgetBack);
        assertThat(reportingDashboardWidgetBack.getDashboar()).isEqualTo(reportingDashboard);

        reportingDashboard.removeWidgets(reportingDashboardWidgetBack);
        assertThat(reportingDashboard.getWidgets()).doesNotContain(reportingDashboardWidgetBack);
        assertThat(reportingDashboardWidgetBack.getDashboar()).isNull();

        reportingDashboard.widgets(new HashSet<>(Set.of(reportingDashboardWidgetBack)));
        assertThat(reportingDashboard.getWidgets()).containsOnly(reportingDashboardWidgetBack);
        assertThat(reportingDashboardWidgetBack.getDashboar()).isEqualTo(reportingDashboard);

        reportingDashboard.setWidgets(new HashSet<>());
        assertThat(reportingDashboard.getWidgets()).doesNotContain(reportingDashboardWidgetBack);
        assertThat(reportingDashboardWidgetBack.getDashboar()).isNull();
    }
}
