package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtractedFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtractedFieldDTO.class);
        ExtractedFieldDTO extractedFieldDTO1 = new ExtractedFieldDTO();
        extractedFieldDTO1.setId(1L);
        ExtractedFieldDTO extractedFieldDTO2 = new ExtractedFieldDTO();
        assertThat(extractedFieldDTO1).isNotEqualTo(extractedFieldDTO2);
        extractedFieldDTO2.setId(extractedFieldDTO1.getId());
        assertThat(extractedFieldDTO1).isEqualTo(extractedFieldDTO2);
        extractedFieldDTO2.setId(2L);
        assertThat(extractedFieldDTO1).isNotEqualTo(extractedFieldDTO2);
        extractedFieldDTO1.setId(null);
        assertThat(extractedFieldDTO1).isNotEqualTo(extractedFieldDTO2);
    }
}
