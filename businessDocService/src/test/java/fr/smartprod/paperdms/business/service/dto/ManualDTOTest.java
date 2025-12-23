package fr.smartprod.paperdms.business.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualDTO.class);
        ManualDTO manualDTO1 = new ManualDTO();
        manualDTO1.setId(1L);
        ManualDTO manualDTO2 = new ManualDTO();
        assertThat(manualDTO1).isNotEqualTo(manualDTO2);
        manualDTO2.setId(manualDTO1.getId());
        assertThat(manualDTO1).isEqualTo(manualDTO2);
        manualDTO2.setId(2L);
        assertThat(manualDTO1).isNotEqualTo(manualDTO2);
        manualDTO1.setId(null);
        assertThat(manualDTO1).isNotEqualTo(manualDTO2);
    }
}
