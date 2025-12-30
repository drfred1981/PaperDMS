package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingPerformanceMetricDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingPerformanceMetricDTO.class);
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO1 = new ReportingPerformanceMetricDTO();
        reportingPerformanceMetricDTO1.setId(1L);
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO2 = new ReportingPerformanceMetricDTO();
        assertThat(reportingPerformanceMetricDTO1).isNotEqualTo(reportingPerformanceMetricDTO2);
        reportingPerformanceMetricDTO2.setId(reportingPerformanceMetricDTO1.getId());
        assertThat(reportingPerformanceMetricDTO1).isEqualTo(reportingPerformanceMetricDTO2);
        reportingPerformanceMetricDTO2.setId(2L);
        assertThat(reportingPerformanceMetricDTO1).isNotEqualTo(reportingPerformanceMetricDTO2);
        reportingPerformanceMetricDTO1.setId(null);
        assertThat(reportingPerformanceMetricDTO1).isNotEqualTo(reportingPerformanceMetricDTO2);
    }
}
