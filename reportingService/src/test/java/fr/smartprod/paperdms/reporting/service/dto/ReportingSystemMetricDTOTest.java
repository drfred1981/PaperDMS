package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingSystemMetricDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingSystemMetricDTO.class);
        ReportingSystemMetricDTO reportingSystemMetricDTO1 = new ReportingSystemMetricDTO();
        reportingSystemMetricDTO1.setId(1L);
        ReportingSystemMetricDTO reportingSystemMetricDTO2 = new ReportingSystemMetricDTO();
        assertThat(reportingSystemMetricDTO1).isNotEqualTo(reportingSystemMetricDTO2);
        reportingSystemMetricDTO2.setId(reportingSystemMetricDTO1.getId());
        assertThat(reportingSystemMetricDTO1).isEqualTo(reportingSystemMetricDTO2);
        reportingSystemMetricDTO2.setId(2L);
        assertThat(reportingSystemMetricDTO1).isNotEqualTo(reportingSystemMetricDTO2);
        reportingSystemMetricDTO1.setId(null);
        assertThat(reportingSystemMetricDTO1).isNotEqualTo(reportingSystemMetricDTO2);
    }
}
