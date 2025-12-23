package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemHealthDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemHealthDTO.class);
        SystemHealthDTO systemHealthDTO1 = new SystemHealthDTO();
        systemHealthDTO1.setId(1L);
        SystemHealthDTO systemHealthDTO2 = new SystemHealthDTO();
        assertThat(systemHealthDTO1).isNotEqualTo(systemHealthDTO2);
        systemHealthDTO2.setId(systemHealthDTO1.getId());
        assertThat(systemHealthDTO1).isEqualTo(systemHealthDTO2);
        systemHealthDTO2.setId(2L);
        assertThat(systemHealthDTO1).isNotEqualTo(systemHealthDTO2);
        systemHealthDTO1.setId(null);
        assertThat(systemHealthDTO1).isNotEqualTo(systemHealthDTO2);
    }
}
