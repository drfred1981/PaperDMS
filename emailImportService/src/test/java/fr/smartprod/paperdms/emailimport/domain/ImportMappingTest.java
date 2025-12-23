package fr.smartprod.paperdms.emailimport.domain;

import static fr.smartprod.paperdms.emailimport.domain.ImportMappingTestSamples.*;
import static fr.smartprod.paperdms.emailimport.domain.ImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImportMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImportMapping.class);
        ImportMapping importMapping1 = getImportMappingSample1();
        ImportMapping importMapping2 = new ImportMapping();
        assertThat(importMapping1).isNotEqualTo(importMapping2);

        importMapping2.setId(importMapping1.getId());
        assertThat(importMapping1).isEqualTo(importMapping2);

        importMapping2 = getImportMappingSample2();
        assertThat(importMapping1).isNotEqualTo(importMapping2);
    }

    @Test
    void ruleTest() {
        ImportMapping importMapping = getImportMappingRandomSampleGenerator();
        ImportRule importRuleBack = getImportRuleRandomSampleGenerator();

        importMapping.setRule(importRuleBack);
        assertThat(importMapping.getRule()).isEqualTo(importRuleBack);

        importMapping.rule(null);
        assertThat(importMapping.getRule()).isNull();
    }
}
