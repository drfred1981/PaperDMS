package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AIAutoTagJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIAutoTagJobDTO.class);
        AIAutoTagJobDTO aIAutoTagJobDTO1 = new AIAutoTagJobDTO();
        aIAutoTagJobDTO1.setId(1L);
        AIAutoTagJobDTO aIAutoTagJobDTO2 = new AIAutoTagJobDTO();
        assertThat(aIAutoTagJobDTO1).isNotEqualTo(aIAutoTagJobDTO2);
        aIAutoTagJobDTO2.setId(aIAutoTagJobDTO1.getId());
        assertThat(aIAutoTagJobDTO1).isEqualTo(aIAutoTagJobDTO2);
        aIAutoTagJobDTO2.setId(2L);
        assertThat(aIAutoTagJobDTO1).isNotEqualTo(aIAutoTagJobDTO2);
        aIAutoTagJobDTO1.setId(null);
        assertThat(aIAutoTagJobDTO1).isNotEqualTo(aIAutoTagJobDTO2);
    }
}
