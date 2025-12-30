package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformMergeJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformMergeJobDTO.class);
        TransformMergeJobDTO transformMergeJobDTO1 = new TransformMergeJobDTO();
        transformMergeJobDTO1.setId(1L);
        TransformMergeJobDTO transformMergeJobDTO2 = new TransformMergeJobDTO();
        assertThat(transformMergeJobDTO1).isNotEqualTo(transformMergeJobDTO2);
        transformMergeJobDTO2.setId(transformMergeJobDTO1.getId());
        assertThat(transformMergeJobDTO1).isEqualTo(transformMergeJobDTO2);
        transformMergeJobDTO2.setId(2L);
        assertThat(transformMergeJobDTO1).isNotEqualTo(transformMergeJobDTO2);
        transformMergeJobDTO1.setId(null);
        assertThat(transformMergeJobDTO1).isNotEqualTo(transformMergeJobDTO2);
    }
}
