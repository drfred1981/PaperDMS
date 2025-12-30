package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentRelationDTO.class);
        DocumentRelationDTO documentRelationDTO1 = new DocumentRelationDTO();
        documentRelationDTO1.setId(1L);
        DocumentRelationDTO documentRelationDTO2 = new DocumentRelationDTO();
        assertThat(documentRelationDTO1).isNotEqualTo(documentRelationDTO2);
        documentRelationDTO2.setId(documentRelationDTO1.getId());
        assertThat(documentRelationDTO1).isEqualTo(documentRelationDTO2);
        documentRelationDTO2.setId(2L);
        assertThat(documentRelationDTO1).isNotEqualTo(documentRelationDTO2);
        documentRelationDTO1.setId(null);
        assertThat(documentRelationDTO1).isNotEqualTo(documentRelationDTO2);
    }
}
