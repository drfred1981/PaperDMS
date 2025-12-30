package fr.smartprod.paperdms.emailimportdocument.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportDocumentDTO.class);
        EmailImportDocumentDTO emailImportDocumentDTO1 = new EmailImportDocumentDTO();
        emailImportDocumentDTO1.setId(1L);
        EmailImportDocumentDTO emailImportDocumentDTO2 = new EmailImportDocumentDTO();
        assertThat(emailImportDocumentDTO1).isNotEqualTo(emailImportDocumentDTO2);
        emailImportDocumentDTO2.setId(emailImportDocumentDTO1.getId());
        assertThat(emailImportDocumentDTO1).isEqualTo(emailImportDocumentDTO2);
        emailImportDocumentDTO2.setId(2L);
        assertThat(emailImportDocumentDTO1).isNotEqualTo(emailImportDocumentDTO2);
        emailImportDocumentDTO1.setId(null);
        assertThat(emailImportDocumentDTO1).isNotEqualTo(emailImportDocumentDTO2);
    }
}
