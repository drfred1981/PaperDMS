package fr.smartprod.paperdms.emailimportdocument.domain;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportEmailAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportEmailAttachment.class);
        EmailImportEmailAttachment emailImportEmailAttachment1 = getEmailImportEmailAttachmentSample1();
        EmailImportEmailAttachment emailImportEmailAttachment2 = new EmailImportEmailAttachment();
        assertThat(emailImportEmailAttachment1).isNotEqualTo(emailImportEmailAttachment2);

        emailImportEmailAttachment2.setId(emailImportEmailAttachment1.getId());
        assertThat(emailImportEmailAttachment1).isEqualTo(emailImportEmailAttachment2);

        emailImportEmailAttachment2 = getEmailImportEmailAttachmentSample2();
        assertThat(emailImportEmailAttachment1).isNotEqualTo(emailImportEmailAttachment2);
    }

    @Test
    void emailImportDocumentTest() {
        EmailImportEmailAttachment emailImportEmailAttachment = getEmailImportEmailAttachmentRandomSampleGenerator();
        EmailImportDocument emailImportDocumentBack = getEmailImportDocumentRandomSampleGenerator();

        emailImportEmailAttachment.setEmailImportDocument(emailImportDocumentBack);
        assertThat(emailImportEmailAttachment.getEmailImportDocument()).isEqualTo(emailImportDocumentBack);

        emailImportEmailAttachment.emailImportDocument(null);
        assertThat(emailImportEmailAttachment.getEmailImportDocument()).isNull();
    }
}
