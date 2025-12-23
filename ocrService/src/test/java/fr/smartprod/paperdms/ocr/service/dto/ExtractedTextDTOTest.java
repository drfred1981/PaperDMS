package fr.smartprod.paperdms.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtractedTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtractedTextDTO.class);
        ExtractedTextDTO extractedTextDTO1 = new ExtractedTextDTO();
        extractedTextDTO1.setId(1L);
        ExtractedTextDTO extractedTextDTO2 = new ExtractedTextDTO();
        assertThat(extractedTextDTO1).isNotEqualTo(extractedTextDTO2);
        extractedTextDTO2.setId(extractedTextDTO1.getId());
        assertThat(extractedTextDTO1).isEqualTo(extractedTextDTO2);
        extractedTextDTO2.setId(2L);
        assertThat(extractedTextDTO1).isNotEqualTo(extractedTextDTO2);
        extractedTextDTO1.setId(null);
        assertThat(extractedTextDTO1).isNotEqualTo(extractedTextDTO2);
    }
}
