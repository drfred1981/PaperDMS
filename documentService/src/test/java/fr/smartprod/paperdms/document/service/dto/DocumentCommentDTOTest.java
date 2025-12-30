package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentCommentDTO.class);
        DocumentCommentDTO documentCommentDTO1 = new DocumentCommentDTO();
        documentCommentDTO1.setId(1L);
        DocumentCommentDTO documentCommentDTO2 = new DocumentCommentDTO();
        assertThat(documentCommentDTO1).isNotEqualTo(documentCommentDTO2);
        documentCommentDTO2.setId(documentCommentDTO1.getId());
        assertThat(documentCommentDTO1).isEqualTo(documentCommentDTO2);
        documentCommentDTO2.setId(2L);
        assertThat(documentCommentDTO1).isNotEqualTo(documentCommentDTO2);
        documentCommentDTO1.setId(null);
        assertThat(documentCommentDTO1).isNotEqualTo(documentCommentDTO2);
    }
}
