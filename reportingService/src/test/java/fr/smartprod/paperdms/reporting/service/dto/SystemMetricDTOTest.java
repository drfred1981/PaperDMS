package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemMetricDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemMetricDTO.class);
        SystemMetricDTO systemMetricDTO1 = new SystemMetricDTO();
        systemMetricDTO1.setId(1L);
        SystemMetricDTO systemMetricDTO2 = new SystemMetricDTO();
        assertThat(systemMetricDTO1).isNotEqualTo(systemMetricDTO2);
        systemMetricDTO2.setId(systemMetricDTO1.getId());
        assertThat(systemMetricDTO1).isEqualTo(systemMetricDTO2);
        systemMetricDTO2.setId(2L);
        assertThat(systemMetricDTO1).isNotEqualTo(systemMetricDTO2);
        systemMetricDTO1.setId(null);
        assertThat(systemMetricDTO1).isNotEqualTo(systemMetricDTO2);
    }
}
