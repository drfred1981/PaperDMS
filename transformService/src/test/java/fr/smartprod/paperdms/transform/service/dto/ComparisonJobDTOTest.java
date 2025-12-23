package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComparisonJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComparisonJobDTO.class);
        ComparisonJobDTO comparisonJobDTO1 = new ComparisonJobDTO();
        comparisonJobDTO1.setId(1L);
        ComparisonJobDTO comparisonJobDTO2 = new ComparisonJobDTO();
        assertThat(comparisonJobDTO1).isNotEqualTo(comparisonJobDTO2);
        comparisonJobDTO2.setId(comparisonJobDTO1.getId());
        assertThat(comparisonJobDTO1).isEqualTo(comparisonJobDTO2);
        comparisonJobDTO2.setId(2L);
        assertThat(comparisonJobDTO1).isNotEqualTo(comparisonJobDTO2);
        comparisonJobDTO1.setId(null);
        assertThat(comparisonJobDTO1).isNotEqualTo(comparisonJobDTO2);
    }
}
