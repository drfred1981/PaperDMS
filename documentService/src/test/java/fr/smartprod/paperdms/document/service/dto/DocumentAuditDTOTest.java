package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentAuditDTO.class);
        DocumentAuditDTO documentAuditDTO1 = new DocumentAuditDTO();
        documentAuditDTO1.setId(1L);
        DocumentAuditDTO documentAuditDTO2 = new DocumentAuditDTO();
        assertThat(documentAuditDTO1).isNotEqualTo(documentAuditDTO2);
        documentAuditDTO2.setId(documentAuditDTO1.getId());
        assertThat(documentAuditDTO1).isEqualTo(documentAuditDTO2);
        documentAuditDTO2.setId(2L);
        assertThat(documentAuditDTO1).isNotEqualTo(documentAuditDTO2);
        documentAuditDTO1.setId(null);
        assertThat(documentAuditDTO1).isNotEqualTo(documentAuditDTO2);
    }
}
