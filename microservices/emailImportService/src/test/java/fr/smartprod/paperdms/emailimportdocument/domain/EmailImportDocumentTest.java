package fr.smartprod.paperdms.emailimportdocument.domain;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachmentTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmailImportDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportDocument.class);
        EmailImportDocument emailImportDocument1 = getEmailImportDocumentSample1();
        EmailImportDocument emailImportDocument2 = new EmailImportDocument();
        assertThat(emailImportDocument1).isNotEqualTo(emailImportDocument2);

        emailImportDocument2.setId(emailImportDocument1.getId());
        assertThat(emailImportDocument1).isEqualTo(emailImportDocument2);

        emailImportDocument2 = getEmailImportDocumentSample2();
        assertThat(emailImportDocument1).isNotEqualTo(emailImportDocument2);
    }

    @Test
    void emailImportEmailAttachmentsTest() {
        EmailImportDocument emailImportDocument = getEmailImportDocumentRandomSampleGenerator();
        EmailImportEmailAttachment emailImportEmailAttachmentBack = getEmailImportEmailAttachmentRandomSampleGenerator();

        emailImportDocument.addEmailImportEmailAttachments(emailImportEmailAttachmentBack);
        assertThat(emailImportDocument.getEmailImportEmailAttachments()).containsOnly(emailImportEmailAttachmentBack);
        assertThat(emailImportEmailAttachmentBack.getEmailImportDocument()).isEqualTo(emailImportDocument);

        emailImportDocument.removeEmailImportEmailAttachments(emailImportEmailAttachmentBack);
        assertThat(emailImportDocument.getEmailImportEmailAttachments()).doesNotContain(emailImportEmailAttachmentBack);
        assertThat(emailImportEmailAttachmentBack.getEmailImportDocument()).isNull();

        emailImportDocument.emailImportEmailAttachments(new HashSet<>(Set.of(emailImportEmailAttachmentBack)));
        assertThat(emailImportDocument.getEmailImportEmailAttachments()).containsOnly(emailImportEmailAttachmentBack);
        assertThat(emailImportEmailAttachmentBack.getEmailImportDocument()).isEqualTo(emailImportDocument);

        emailImportDocument.setEmailImportEmailAttachments(new HashSet<>());
        assertThat(emailImportDocument.getEmailImportEmailAttachments()).doesNotContain(emailImportEmailAttachmentBack);
        assertThat(emailImportEmailAttachmentBack.getEmailImportDocument()).isNull();
    }

    @Test
    void appliedRuleTest() {
        EmailImportDocument emailImportDocument = getEmailImportDocumentRandomSampleGenerator();
        EmailImportImportRule emailImportImportRuleBack = getEmailImportImportRuleRandomSampleGenerator();

        emailImportDocument.setAppliedRule(emailImportImportRuleBack);
        assertThat(emailImportDocument.getAppliedRule()).isEqualTo(emailImportImportRuleBack);

        emailImportDocument.appliedRule(null);
        assertThat(emailImportDocument.getAppliedRule()).isNull();
    }
}
