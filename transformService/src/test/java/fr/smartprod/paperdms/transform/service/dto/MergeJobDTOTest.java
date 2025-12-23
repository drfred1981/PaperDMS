package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MergeJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MergeJobDTO.class);
        MergeJobDTO mergeJobDTO1 = new MergeJobDTO();
        mergeJobDTO1.setId(1L);
        MergeJobDTO mergeJobDTO2 = new MergeJobDTO();
        assertThat(mergeJobDTO1).isNotEqualTo(mergeJobDTO2);
        mergeJobDTO2.setId(mergeJobDTO1.getId());
        assertThat(mergeJobDTO1).isEqualTo(mergeJobDTO2);
        mergeJobDTO2.setId(2L);
        assertThat(mergeJobDTO1).isNotEqualTo(mergeJobDTO2);
        mergeJobDTO1.setId(null);
        assertThat(mergeJobDTO1).isNotEqualTo(mergeJobDTO2);
    }
}
