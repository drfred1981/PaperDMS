package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransformRedactionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransformRedactionJobDTO.class);
        TransformRedactionJobDTO transformRedactionJobDTO1 = new TransformRedactionJobDTO();
        transformRedactionJobDTO1.setId(1L);
        TransformRedactionJobDTO transformRedactionJobDTO2 = new TransformRedactionJobDTO();
        assertThat(transformRedactionJobDTO1).isNotEqualTo(transformRedactionJobDTO2);
        transformRedactionJobDTO2.setId(transformRedactionJobDTO1.getId());
        assertThat(transformRedactionJobDTO1).isEqualTo(transformRedactionJobDTO2);
        transformRedactionJobDTO2.setId(2L);
        assertThat(transformRedactionJobDTO1).isNotEqualTo(transformRedactionJobDTO2);
        transformRedactionJobDTO1.setId(null);
        assertThat(transformRedactionJobDTO1).isNotEqualTo(transformRedactionJobDTO2);
    }
}
