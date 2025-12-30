package fr.smartprod.paperdms.ocr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ocr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrcExtractedTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrcExtractedTextDTO.class);
        OrcExtractedTextDTO orcExtractedTextDTO1 = new OrcExtractedTextDTO();
        orcExtractedTextDTO1.setId(1L);
        OrcExtractedTextDTO orcExtractedTextDTO2 = new OrcExtractedTextDTO();
        assertThat(orcExtractedTextDTO1).isNotEqualTo(orcExtractedTextDTO2);
        orcExtractedTextDTO2.setId(orcExtractedTextDTO1.getId());
        assertThat(orcExtractedTextDTO1).isEqualTo(orcExtractedTextDTO2);
        orcExtractedTextDTO2.setId(2L);
        assertThat(orcExtractedTextDTO1).isNotEqualTo(orcExtractedTextDTO2);
        orcExtractedTextDTO1.setId(null);
        assertThat(orcExtractedTextDTO1).isNotEqualTo(orcExtractedTextDTO2);
    }
}
