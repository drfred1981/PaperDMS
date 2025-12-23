package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerformanceMetricDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceMetricDTO.class);
        PerformanceMetricDTO performanceMetricDTO1 = new PerformanceMetricDTO();
        performanceMetricDTO1.setId(1L);
        PerformanceMetricDTO performanceMetricDTO2 = new PerformanceMetricDTO();
        assertThat(performanceMetricDTO1).isNotEqualTo(performanceMetricDTO2);
        performanceMetricDTO2.setId(performanceMetricDTO1.getId());
        assertThat(performanceMetricDTO1).isEqualTo(performanceMetricDTO2);
        performanceMetricDTO2.setId(2L);
        assertThat(performanceMetricDTO1).isNotEqualTo(performanceMetricDTO2);
        performanceMetricDTO1.setId(null);
        assertThat(performanceMetricDTO1).isNotEqualTo(performanceMetricDTO2);
    }
}
