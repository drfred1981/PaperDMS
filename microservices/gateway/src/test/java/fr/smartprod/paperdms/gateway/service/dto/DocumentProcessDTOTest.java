package fr.smartprod.paperdms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentProcessDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentProcessDTO.class);
        DocumentProcessDTO documentProcessDTO1 = new DocumentProcessDTO();
        documentProcessDTO1.setId(1L);
        DocumentProcessDTO documentProcessDTO2 = new DocumentProcessDTO();
        assertThat(documentProcessDTO1).isNotEqualTo(documentProcessDTO2);
        documentProcessDTO2.setId(documentProcessDTO1.getId());
        assertThat(documentProcessDTO1).isEqualTo(documentProcessDTO2);
        documentProcessDTO2.setId(2L);
        assertThat(documentProcessDTO1).isNotEqualTo(documentProcessDTO2);
        documentProcessDTO1.setId(null);
        assertThat(documentProcessDTO1).isNotEqualTo(documentProcessDTO2);
    }
}
