package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentPermissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentPermissionDTO.class);
        DocumentPermissionDTO documentPermissionDTO1 = new DocumentPermissionDTO();
        documentPermissionDTO1.setId(1L);
        DocumentPermissionDTO documentPermissionDTO2 = new DocumentPermissionDTO();
        assertThat(documentPermissionDTO1).isNotEqualTo(documentPermissionDTO2);
        documentPermissionDTO2.setId(documentPermissionDTO1.getId());
        assertThat(documentPermissionDTO1).isEqualTo(documentPermissionDTO2);
        documentPermissionDTO2.setId(2L);
        assertThat(documentPermissionDTO1).isNotEqualTo(documentPermissionDTO2);
        documentPermissionDTO1.setId(null);
        assertThat(documentPermissionDTO1).isNotEqualTo(documentPermissionDTO2);
    }
}
