package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentExtractedFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentExtractedFieldDTO.class);
        DocumentExtractedFieldDTO documentExtractedFieldDTO1 = new DocumentExtractedFieldDTO();
        documentExtractedFieldDTO1.setId(1L);
        DocumentExtractedFieldDTO documentExtractedFieldDTO2 = new DocumentExtractedFieldDTO();
        assertThat(documentExtractedFieldDTO1).isNotEqualTo(documentExtractedFieldDTO2);
        documentExtractedFieldDTO2.setId(documentExtractedFieldDTO1.getId());
        assertThat(documentExtractedFieldDTO1).isEqualTo(documentExtractedFieldDTO2);
        documentExtractedFieldDTO2.setId(2L);
        assertThat(documentExtractedFieldDTO1).isNotEqualTo(documentExtractedFieldDTO2);
        documentExtractedFieldDTO1.setId(null);
        assertThat(documentExtractedFieldDTO1).isNotEqualTo(documentExtractedFieldDTO2);
    }
}
