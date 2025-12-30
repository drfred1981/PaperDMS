package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingDashboardWidgetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboardWidgetDTO.class);
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO1 = new ReportingDashboardWidgetDTO();
        reportingDashboardWidgetDTO1.setId(1L);
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO2 = new ReportingDashboardWidgetDTO();
        assertThat(reportingDashboardWidgetDTO1).isNotEqualTo(reportingDashboardWidgetDTO2);
        reportingDashboardWidgetDTO2.setId(reportingDashboardWidgetDTO1.getId());
        assertThat(reportingDashboardWidgetDTO1).isEqualTo(reportingDashboardWidgetDTO2);
        reportingDashboardWidgetDTO2.setId(2L);
        assertThat(reportingDashboardWidgetDTO1).isNotEqualTo(reportingDashboardWidgetDTO2);
        reportingDashboardWidgetDTO1.setId(null);
        assertThat(reportingDashboardWidgetDTO1).isNotEqualTo(reportingDashboardWidgetDTO2);
    }
}
