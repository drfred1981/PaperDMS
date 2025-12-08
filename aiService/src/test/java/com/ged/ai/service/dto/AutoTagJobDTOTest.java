package com.ged.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutoTagJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoTagJobDTO.class);
        AutoTagJobDTO autoTagJobDTO1 = new AutoTagJobDTO();
        autoTagJobDTO1.setId(1L);
        AutoTagJobDTO autoTagJobDTO2 = new AutoTagJobDTO();
        assertThat(autoTagJobDTO1).isNotEqualTo(autoTagJobDTO2);
        autoTagJobDTO2.setId(autoTagJobDTO1.getId());
        assertThat(autoTagJobDTO1).isEqualTo(autoTagJobDTO2);
        autoTagJobDTO2.setId(2L);
        assertThat(autoTagJobDTO1).isNotEqualTo(autoTagJobDTO2);
        autoTagJobDTO1.setId(null);
        assertThat(autoTagJobDTO1).isNotEqualTo(autoTagJobDTO2);
    }
}
