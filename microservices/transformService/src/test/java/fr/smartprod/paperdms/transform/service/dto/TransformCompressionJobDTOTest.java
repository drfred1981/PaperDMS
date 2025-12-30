package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformCompressionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformCompressionJobDTO.class);
        TransformCompressionJobDTO transformCompressionJobDTO1 = new TransformCompressionJobDTO();
        transformCompressionJobDTO1.setId(1L);
        TransformCompressionJobDTO transformCompressionJobDTO2 = new TransformCompressionJobDTO();
        assertThat(transformCompressionJobDTO1).isNotEqualTo(transformCompressionJobDTO2);
        transformCompressionJobDTO2.setId(transformCompressionJobDTO1.getId());
        assertThat(transformCompressionJobDTO1).isEqualTo(transformCompressionJobDTO2);
        transformCompressionJobDTO2.setId(2L);
        assertThat(transformCompressionJobDTO1).isNotEqualTo(transformCompressionJobDTO2);
        transformCompressionJobDTO1.setId(null);
        assertThat(transformCompressionJobDTO1).isNotEqualTo(transformCompressionJobDTO2);
    }
}
