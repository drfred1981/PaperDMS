package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTagDTO.class);
        DocumentTagDTO documentTagDTO1 = new DocumentTagDTO();
        documentTagDTO1.setId(1L);
        DocumentTagDTO documentTagDTO2 = new DocumentTagDTO();
        assertThat(documentTagDTO1).isNotEqualTo(documentTagDTO2);
        documentTagDTO2.setId(documentTagDTO1.getId());
        assertThat(documentTagDTO1).isEqualTo(documentTagDTO2);
        documentTagDTO2.setId(2L);
        assertThat(documentTagDTO1).isNotEqualTo(documentTagDTO2);
        documentTagDTO1.setId(null);
        assertThat(documentTagDTO1).isNotEqualTo(documentTagDTO2);
    }
}
