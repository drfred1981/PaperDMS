package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformWatermarkJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformWatermarkJobDTO.class);
        TransformWatermarkJobDTO transformWatermarkJobDTO1 = new TransformWatermarkJobDTO();
        transformWatermarkJobDTO1.setId(1L);
        TransformWatermarkJobDTO transformWatermarkJobDTO2 = new TransformWatermarkJobDTO();
        assertThat(transformWatermarkJobDTO1).isNotEqualTo(transformWatermarkJobDTO2);
        transformWatermarkJobDTO2.setId(transformWatermarkJobDTO1.getId());
        assertThat(transformWatermarkJobDTO1).isEqualTo(transformWatermarkJobDTO2);
        transformWatermarkJobDTO2.setId(2L);
        assertThat(transformWatermarkJobDTO1).isNotEqualTo(transformWatermarkJobDTO2);
        transformWatermarkJobDTO1.setId(null);
        assertThat(transformWatermarkJobDTO1).isNotEqualTo(transformWatermarkJobDTO2);
    }
}
