package fr.smartprod.paperdms.emailimportdocument.domain;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMappingTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmailImportImportRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportImportRule.class);
        EmailImportImportRule emailImportImportRule1 = getEmailImportImportRuleSample1();
        EmailImportImportRule emailImportImportRule2 = new EmailImportImportRule();
        assertThat(emailImportImportRule1).isNotEqualTo(emailImportImportRule2);

        emailImportImportRule2.setId(emailImportImportRule1.getId());
        assertThat(emailImportImportRule1).isEqualTo(emailImportImportRule2);

        emailImportImportRule2 = getEmailImportImportRuleSample2();
        assertThat(emailImportImportRule1).isNotEqualTo(emailImportImportRule2);
    }

    @Test
    void emailImportImportMappingsTest() {
        EmailImportImportRule emailImportImportRule = getEmailImportImportRuleRandomSampleGenerator();
        EmailImportImportMapping emailImportImportMappingBack = getEmailImportImportMappingRandomSampleGenerator();

        emailImportImportRule.addEmailImportImportMappings(emailImportImportMappingBack);
        assertThat(emailImportImportRule.getEmailImportImportMappings()).containsOnly(emailImportImportMappingBack);
        assertThat(emailImportImportMappingBack.getRule()).isEqualTo(emailImportImportRule);

        emailImportImportRule.removeEmailImportImportMappings(emailImportImportMappingBack);
        assertThat(emailImportImportRule.getEmailImportImportMappings()).doesNotContain(emailImportImportMappingBack);
        assertThat(emailImportImportMappingBack.getRule()).isNull();

        emailImportImportRule.emailImportImportMappings(new HashSet<>(Set.of(emailImportImportMappingBack)));
        assertThat(emailImportImportRule.getEmailImportImportMappings()).containsOnly(emailImportImportMappingBack);
        assertThat(emailImportImportMappingBack.getRule()).isEqualTo(emailImportImportRule);

        emailImportImportRule.setEmailImportImportMappings(new HashSet<>());
        assertThat(emailImportImportRule.getEmailImportImportMappings()).doesNotContain(emailImportImportMappingBack);
        assertThat(emailImportImportMappingBack.getRule()).isNull();
    }

    @Test
    void emailImportDocumentsTest() {
        EmailImportImportRule emailImportImportRule = getEmailImportImportRuleRandomSampleGenerator();
        EmailImportDocument emailImportDocumentBack = getEmailImportDocumentRandomSampleGenerator();

        emailImportImportRule.addEmailImportDocuments(emailImportDocumentBack);
        assertThat(emailImportImportRule.getEmailImportDocuments()).containsOnly(emailImportDocumentBack);
        assertThat(emailImportDocumentBack.getAppliedRule()).isEqualTo(emailImportImportRule);

        emailImportImportRule.removeEmailImportDocuments(emailImportDocumentBack);
        assertThat(emailImportImportRule.getEmailImportDocuments()).doesNotContain(emailImportDocumentBack);
        assertThat(emailImportDocumentBack.getAppliedRule()).isNull();

        emailImportImportRule.emailImportDocuments(new HashSet<>(Set.of(emailImportDocumentBack)));
        assertThat(emailImportImportRule.getEmailImportDocuments()).containsOnly(emailImportDocumentBack);
        assertThat(emailImportDocumentBack.getAppliedRule()).isEqualTo(emailImportImportRule);

        emailImportImportRule.setEmailImportDocuments(new HashSet<>());
        assertThat(emailImportImportRule.getEmailImportDocuments()).doesNotContain(emailImportDocumentBack);
        assertThat(emailImportDocumentBack.getAppliedRule()).isNull();
    }
}
