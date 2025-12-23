package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedactionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedactionJobDTO.class);
        RedactionJobDTO redactionJobDTO1 = new RedactionJobDTO();
        redactionJobDTO1.setId(1L);
        RedactionJobDTO redactionJobDTO2 = new RedactionJobDTO();
        assertThat(redactionJobDTO1).isNotEqualTo(redactionJobDTO2);
        redactionJobDTO2.setId(redactionJobDTO1.getId());
        assertThat(redactionJobDTO1).isEqualTo(redactionJobDTO2);
        redactionJobDTO2.setId(2L);
        assertThat(redactionJobDTO1).isNotEqualTo(redactionJobDTO2);
        redactionJobDTO1.setId(null);
        assertThat(redactionJobDTO1).isNotEqualTo(redactionJobDTO2);
    }
}
