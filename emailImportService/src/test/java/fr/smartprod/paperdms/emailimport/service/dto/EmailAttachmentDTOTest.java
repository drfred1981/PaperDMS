package fr.smartprod.paperdms.emailimport.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailAttachmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailAttachmentDTO.class);
        EmailAttachmentDTO emailAttachmentDTO1 = new EmailAttachmentDTO();
        emailAttachmentDTO1.setId(1L);
        EmailAttachmentDTO emailAttachmentDTO2 = new EmailAttachmentDTO();
        assertThat(emailAttachmentDTO1).isNotEqualTo(emailAttachmentDTO2);
        emailAttachmentDTO2.setId(emailAttachmentDTO1.getId());
        assertThat(emailAttachmentDTO1).isEqualTo(emailAttachmentDTO2);
        emailAttachmentDTO2.setId(2L);
        assertThat(emailAttachmentDTO1).isNotEqualTo(emailAttachmentDTO2);
        emailAttachmentDTO1.setId(null);
        assertThat(emailAttachmentDTO1).isNotEqualTo(emailAttachmentDTO2);
    }
}
