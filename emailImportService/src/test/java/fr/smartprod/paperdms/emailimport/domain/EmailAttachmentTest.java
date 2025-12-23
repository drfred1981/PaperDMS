package fr.smartprod.paperdms.emailimport.domain;

import static fr.smartprod.paperdms.emailimport.domain.EmailAttachmentTestSamples.*;
import static fr.smartprod.paperdms.emailimport.domain.EmailImportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailAttachment.class);
        EmailAttachment emailAttachment1 = getEmailAttachmentSample1();
        EmailAttachment emailAttachment2 = new EmailAttachment();
        assertThat(emailAttachment1).isNotEqualTo(emailAttachment2);

        emailAttachment2.setId(emailAttachment1.getId());
        assertThat(emailAttachment1).isEqualTo(emailAttachment2);

        emailAttachment2 = getEmailAttachmentSample2();
        assertThat(emailAttachment1).isNotEqualTo(emailAttachment2);
    }

    @Test
    void emailImportTest() {
        EmailAttachment emailAttachment = getEmailAttachmentRandomSampleGenerator();
        EmailImport emailImportBack = getEmailImportRandomSampleGenerator();

        emailAttachment.setEmailImport(emailImportBack);
        assertThat(emailAttachment.getEmailImport()).isEqualTo(emailImportBack);

        emailAttachment.emailImport(null);
        assertThat(emailAttachment.getEmailImport()).isNull();
    }
}
