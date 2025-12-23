package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DashboardWidgetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DashboardWidgetDTO.class);
        DashboardWidgetDTO dashboardWidgetDTO1 = new DashboardWidgetDTO();
        dashboardWidgetDTO1.setId(1L);
        DashboardWidgetDTO dashboardWidgetDTO2 = new DashboardWidgetDTO();
        assertThat(dashboardWidgetDTO1).isNotEqualTo(dashboardWidgetDTO2);
        dashboardWidgetDTO2.setId(dashboardWidgetDTO1.getId());
        assertThat(dashboardWidgetDTO1).isEqualTo(dashboardWidgetDTO2);
        dashboardWidgetDTO2.setId(2L);
        assertThat(dashboardWidgetDTO1).isNotEqualTo(dashboardWidgetDTO2);
        dashboardWidgetDTO1.setId(null);
        assertThat(dashboardWidgetDTO1).isNotEqualTo(dashboardWidgetDTO2);
    }
}
