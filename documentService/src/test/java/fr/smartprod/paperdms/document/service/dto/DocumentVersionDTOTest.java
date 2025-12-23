package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentVersionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentVersionDTO.class);
        DocumentVersionDTO documentVersionDTO1 = new DocumentVersionDTO();
        documentVersionDTO1.setId(1L);
        DocumentVersionDTO documentVersionDTO2 = new DocumentVersionDTO();
        assertThat(documentVersionDTO1).isNotEqualTo(documentVersionDTO2);
        documentVersionDTO2.setId(documentVersionDTO1.getId());
        assertThat(documentVersionDTO1).isEqualTo(documentVersionDTO2);
        documentVersionDTO2.setId(2L);
        assertThat(documentVersionDTO1).isNotEqualTo(documentVersionDTO2);
        documentVersionDTO1.setId(null);
        assertThat(documentVersionDTO1).isNotEqualTo(documentVersionDTO2);
    }
}
