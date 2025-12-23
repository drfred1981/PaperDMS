package fr.smartprod.paperdms.reporting.domain;

import static fr.smartprod.paperdms.reporting.domain.DashboardTestSamples.*;
import static fr.smartprod.paperdms.reporting.domain.DashboardWidgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DashboardWidgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DashboardWidget.class);
        DashboardWidget dashboardWidget1 = getDashboardWidgetSample1();
        DashboardWidget dashboardWidget2 = new DashboardWidget();
        assertThat(dashboardWidget1).isNotEqualTo(dashboardWidget2);

        dashboardWidget2.setId(dashboardWidget1.getId());
        assertThat(dashboardWidget1).isEqualTo(dashboardWidget2);

        dashboardWidget2 = getDashboardWidgetSample2();
        assertThat(dashboardWidget1).isNotEqualTo(dashboardWidget2);
    }

    @Test
    void dashboardTest() {
        DashboardWidget dashboardWidget = getDashboardWidgetRandomSampleGenerator();
        Dashboard dashboardBack = getDashboardRandomSampleGenerator();

        dashboardWidget.setDashboard(dashboardBack);
        assertThat(dashboardWidget.getDashboard()).isEqualTo(dashboardBack);

        dashboardWidget.dashboard(null);
        assertThat(dashboardWidget.getDashboard()).isNull();
    }
}
