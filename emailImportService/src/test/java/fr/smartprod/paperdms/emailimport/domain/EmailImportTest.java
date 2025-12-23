package fr.smartprod.paperdms.emailimport.domain;

import static fr.smartprod.paperdms.emailimport.domain.EmailImportTestSamples.*;
import static fr.smartprod.paperdms.emailimport.domain.ImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImport.class);
        EmailImport emailImport1 = getEmailImportSample1();
        EmailImport emailImport2 = new EmailImport();
        assertThat(emailImport1).isNotEqualTo(emailImport2);

        emailImport2.setId(emailImport1.getId());
        assertThat(emailImport1).isEqualTo(emailImport2);

        emailImport2 = getEmailImportSample2();
        assertThat(emailImport1).isNotEqualTo(emailImport2);
    }

    @Test
    void appliedRuleTest() {
        EmailImport emailImport = getEmailImportRandomSampleGenerator();
        ImportRule importRuleBack = getImportRuleRandomSampleGenerator();

        emailImport.setAppliedRule(importRuleBack);
        assertThat(emailImport.getAppliedRule()).isEqualTo(importRuleBack);

        emailImport.appliedRule(null);
        assertThat(emailImport.getAppliedRule()).isNull();
    }
}
