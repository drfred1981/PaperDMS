package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentServiceStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentServiceStatusDTO.class);
        DocumentServiceStatusDTO documentServiceStatusDTO1 = new DocumentServiceStatusDTO();
        documentServiceStatusDTO1.setId(1L);
        DocumentServiceStatusDTO documentServiceStatusDTO2 = new DocumentServiceStatusDTO();
        assertThat(documentServiceStatusDTO1).isNotEqualTo(documentServiceStatusDTO2);
        documentServiceStatusDTO2.setId(documentServiceStatusDTO1.getId());
        assertThat(documentServiceStatusDTO1).isEqualTo(documentServiceStatusDTO2);
        documentServiceStatusDTO2.setId(2L);
        assertThat(documentServiceStatusDTO1).isNotEqualTo(documentServiceStatusDTO2);
        documentServiceStatusDTO1.setId(null);
        assertThat(documentServiceStatusDTO1).isNotEqualTo(documentServiceStatusDTO2);
    }
}
