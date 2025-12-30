package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformConversionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformConversionJobDTO.class);
        TransformConversionJobDTO transformConversionJobDTO1 = new TransformConversionJobDTO();
        transformConversionJobDTO1.setId(1L);
        TransformConversionJobDTO transformConversionJobDTO2 = new TransformConversionJobDTO();
        assertThat(transformConversionJobDTO1).isNotEqualTo(transformConversionJobDTO2);
        transformConversionJobDTO2.setId(transformConversionJobDTO1.getId());
        assertThat(transformConversionJobDTO1).isEqualTo(transformConversionJobDTO2);
        transformConversionJobDTO2.setId(2L);
        assertThat(transformConversionJobDTO1).isNotEqualTo(transformConversionJobDTO2);
        transformConversionJobDTO1.setId(null);
        assertThat(transformConversionJobDTO1).isNotEqualTo(transformConversionJobDTO2);
    }
}
