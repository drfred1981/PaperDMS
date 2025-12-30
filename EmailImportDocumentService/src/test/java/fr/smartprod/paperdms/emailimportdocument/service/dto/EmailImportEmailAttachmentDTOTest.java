package fr.smartprod.paperdms.emailimportdocument.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportEmailAttachmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportEmailAttachmentDTO.class);
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO1 = new EmailImportEmailAttachmentDTO();
        emailImportEmailAttachmentDTO1.setId(1L);
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO2 = new EmailImportEmailAttachmentDTO();
        assertThat(emailImportEmailAttachmentDTO1).isNotEqualTo(emailImportEmailAttachmentDTO2);
        emailImportEmailAttachmentDTO2.setId(emailImportEmailAttachmentDTO1.getId());
        assertThat(emailImportEmailAttachmentDTO1).isEqualTo(emailImportEmailAttachmentDTO2);
        emailImportEmailAttachmentDTO2.setId(2L);
        assertThat(emailImportEmailAttachmentDTO1).isNotEqualTo(emailImportEmailAttachmentDTO2);
        emailImportEmailAttachmentDTO1.setId(null);
        assertThat(emailImportEmailAttachmentDTO1).isNotEqualTo(emailImportEmailAttachmentDTO2);
    }
}
