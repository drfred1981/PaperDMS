package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AICacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AICacheDTO.class);
        AICacheDTO aICacheDTO1 = new AICacheDTO();
        aICacheDTO1.setId(1L);
        AICacheDTO aICacheDTO2 = new AICacheDTO();
        assertThat(aICacheDTO1).isNotEqualTo(aICacheDTO2);
        aICacheDTO2.setId(aICacheDTO1.getId());
        assertThat(aICacheDTO1).isEqualTo(aICacheDTO2);
        aICacheDTO2.setId(2L);
        assertThat(aICacheDTO1).isNotEqualTo(aICacheDTO2);
        aICacheDTO1.setId(null);
        assertThat(aICacheDTO1).isNotEqualTo(aICacheDTO2);
    }
}
