package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceStatusDTO.class);
        ServiceStatusDTO serviceStatusDTO1 = new ServiceStatusDTO();
        serviceStatusDTO1.setId(1L);
        ServiceStatusDTO serviceStatusDTO2 = new ServiceStatusDTO();
        assertThat(serviceStatusDTO1).isNotEqualTo(serviceStatusDTO2);
        serviceStatusDTO2.setId(serviceStatusDTO1.getId());
        assertThat(serviceStatusDTO1).isEqualTo(serviceStatusDTO2);
        serviceStatusDTO2.setId(2L);
        assertThat(serviceStatusDTO1).isNotEqualTo(serviceStatusDTO2);
        serviceStatusDTO1.setId(null);
        assertThat(serviceStatusDTO1).isNotEqualTo(serviceStatusDTO2);
    }
}
