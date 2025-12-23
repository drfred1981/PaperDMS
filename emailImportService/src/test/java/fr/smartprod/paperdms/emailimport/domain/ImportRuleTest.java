package fr.smartprod.paperdms.emailimport.domain;

import static fr.smartprod.paperdms.emailimport.domain.ImportRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImportRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImportRule.class);
        ImportRule importRule1 = getImportRuleSample1();
        ImportRule importRule2 = new ImportRule();
        assertThat(importRule1).isNotEqualTo(importRule2);

        importRule2.setId(importRule1.getId());
        assertThat(importRule1).isEqualTo(importRule2);

        importRule2 = getImportRuleSample2();
        assertThat(importRule1).isNotEqualTo(importRule2);
    }
}
