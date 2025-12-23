package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AiCacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AiCacheDTO.class);
        AiCacheDTO aiCacheDTO1 = new AiCacheDTO();
        aiCacheDTO1.setId(1L);
        AiCacheDTO aiCacheDTO2 = new AiCacheDTO();
        assertThat(aiCacheDTO1).isNotEqualTo(aiCacheDTO2);
        aiCacheDTO2.setId(aiCacheDTO1.getId());
        assertThat(aiCacheDTO1).isEqualTo(aiCacheDTO2);
        aiCacheDTO2.setId(2L);
        assertThat(aiCacheDTO1).isNotEqualTo(aiCacheDTO2);
        aiCacheDTO1.setId(null);
        assertThat(aiCacheDTO1).isNotEqualTo(aiCacheDTO2);
    }
}
