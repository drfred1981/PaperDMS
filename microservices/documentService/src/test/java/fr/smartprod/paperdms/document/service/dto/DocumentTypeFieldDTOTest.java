package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTypeFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTypeFieldDTO.class);
        DocumentTypeFieldDTO documentTypeFieldDTO1 = new DocumentTypeFieldDTO();
        documentTypeFieldDTO1.setId(1L);
        DocumentTypeFieldDTO documentTypeFieldDTO2 = new DocumentTypeFieldDTO();
        assertThat(documentTypeFieldDTO1).isNotEqualTo(documentTypeFieldDTO2);
        documentTypeFieldDTO2.setId(documentTypeFieldDTO1.getId());
        assertThat(documentTypeFieldDTO1).isEqualTo(documentTypeFieldDTO2);
        documentTypeFieldDTO2.setId(2L);
        assertThat(documentTypeFieldDTO1).isNotEqualTo(documentTypeFieldDTO2);
        documentTypeFieldDTO1.setId(null);
        assertThat(documentTypeFieldDTO1).isNotEqualTo(documentTypeFieldDTO2);
    }
}
