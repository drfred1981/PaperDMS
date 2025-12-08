package com.ged.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TikaConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TikaConfigurationDTO.class);
        TikaConfigurationDTO tikaConfigurationDTO1 = new TikaConfigurationDTO();
        tikaConfigurationDTO1.setId(1L);
        TikaConfigurationDTO tikaConfigurationDTO2 = new TikaConfigurationDTO();
        assertThat(tikaConfigurationDTO1).isNotEqualTo(tikaConfigurationDTO2);
        tikaConfigurationDTO2.setId(tikaConfigurationDTO1.getId());
        assertThat(tikaConfigurationDTO1).isEqualTo(tikaConfigurationDTO2);
        tikaConfigurationDTO2.setId(2L);
        assertThat(tikaConfigurationDTO1).isNotEqualTo(tikaConfigurationDTO2);
        tikaConfigurationDTO1.setId(null);
        assertThat(tikaConfigurationDTO1).isNotEqualTo(tikaConfigurationDTO2);
    }
}
