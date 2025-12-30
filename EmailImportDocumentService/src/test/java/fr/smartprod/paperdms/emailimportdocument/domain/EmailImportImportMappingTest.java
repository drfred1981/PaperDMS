package fr.smartprod.paperdms.emailimportdocument.domain;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMappingTestSamples.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportImportMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportImportMapping.class);
        EmailImportImportMapping emailImportImportMapping1 = getEmailImportImportMappingSample1();
        EmailImportImportMapping emailImportImportMapping2 = new EmailImportImportMapping();
        assertThat(emailImportImportMapping1).isNotEqualTo(emailImportImportMapping2);

        emailImportImportMapping2.setId(emailImportImportMapping1.getId());
        assertThat(emailImportImportMapping1).isEqualTo(emailImportImportMapping2);

        emailImportImportMapping2 = getEmailImportImportMappingSample2();
        assertThat(emailImportImportMapping1).isNotEqualTo(emailImportImportMapping2);
    }

    @Test
    void ruleTest() {
        EmailImportImportMapping emailImportImportMapping = getEmailImportImportMappingRandomSampleGenerator();
        EmailImportImportRule emailImportImportRuleBack = getEmailImportImportRuleRandomSampleGenerator();

        emailImportImportMapping.setRule(emailImportImportRuleBack);
        assertThat(emailImportImportMapping.getRule()).isEqualTo(emailImportImportRuleBack);

        emailImportImportMapping.rule(null);
        assertThat(emailImportImportMapping.getRule()).isNull();
    }
}
