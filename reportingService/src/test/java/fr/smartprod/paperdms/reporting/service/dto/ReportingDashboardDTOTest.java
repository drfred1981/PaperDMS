package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingDashboardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDashboardDTO.class);
        ReportingDashboardDTO reportingDashboardDTO1 = new ReportingDashboardDTO();
        reportingDashboardDTO1.setId(1L);
        ReportingDashboardDTO reportingDashboardDTO2 = new ReportingDashboardDTO();
        assertThat(reportingDashboardDTO1).isNotEqualTo(reportingDashboardDTO2);
        reportingDashboardDTO2.setId(reportingDashboardDTO1.getId());
        assertThat(reportingDashboardDTO1).isEqualTo(reportingDashboardDTO2);
        reportingDashboardDTO2.setId(2L);
        assertThat(reportingDashboardDTO1).isNotEqualTo(reportingDashboardDTO2);
        reportingDashboardDTO1.setId(null);
        assertThat(reportingDashboardDTO1).isNotEqualTo(reportingDashboardDTO2);
    }
}
