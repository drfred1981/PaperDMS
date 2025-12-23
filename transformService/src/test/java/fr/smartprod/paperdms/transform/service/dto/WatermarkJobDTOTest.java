package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatermarkJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatermarkJobDTO.class);
        WatermarkJobDTO watermarkJobDTO1 = new WatermarkJobDTO();
        watermarkJobDTO1.setId(1L);
        WatermarkJobDTO watermarkJobDTO2 = new WatermarkJobDTO();
        assertThat(watermarkJobDTO1).isNotEqualTo(watermarkJobDTO2);
        watermarkJobDTO2.setId(watermarkJobDTO1.getId());
        assertThat(watermarkJobDTO1).isEqualTo(watermarkJobDTO2);
        watermarkJobDTO2.setId(2L);
        assertThat(watermarkJobDTO1).isNotEqualTo(watermarkJobDTO2);
        watermarkJobDTO1.setId(null);
        assertThat(watermarkJobDTO1).isNotEqualTo(watermarkJobDTO2);
    }
}
