package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTemplateDTO.class);
        DocumentTemplateDTO documentTemplateDTO1 = new DocumentTemplateDTO();
        documentTemplateDTO1.setId(1L);
        DocumentTemplateDTO documentTemplateDTO2 = new DocumentTemplateDTO();
        assertThat(documentTemplateDTO1).isNotEqualTo(documentTemplateDTO2);
        documentTemplateDTO2.setId(documentTemplateDTO1.getId());
        assertThat(documentTemplateDTO1).isEqualTo(documentTemplateDTO2);
        documentTemplateDTO2.setId(2L);
        assertThat(documentTemplateDTO1).isNotEqualTo(documentTemplateDTO2);
        documentTemplateDTO1.setId(null);
        assertThat(documentTemplateDTO1).isNotEqualTo(documentTemplateDTO2);
    }
}
