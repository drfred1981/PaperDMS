package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConversionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConversionJobDTO.class);
        ConversionJobDTO conversionJobDTO1 = new ConversionJobDTO();
        conversionJobDTO1.setId(1L);
        ConversionJobDTO conversionJobDTO2 = new ConversionJobDTO();
        assertThat(conversionJobDTO1).isNotEqualTo(conversionJobDTO2);
        conversionJobDTO2.setId(conversionJobDTO1.getId());
        assertThat(conversionJobDTO1).isEqualTo(conversionJobDTO2);
        conversionJobDTO2.setId(2L);
        assertThat(conversionJobDTO1).isNotEqualTo(conversionJobDTO2);
        conversionJobDTO1.setId(null);
        assertThat(conversionJobDTO1).isNotEqualTo(conversionJobDTO2);
    }
}
