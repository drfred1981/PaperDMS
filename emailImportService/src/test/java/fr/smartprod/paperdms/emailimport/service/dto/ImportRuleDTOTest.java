package fr.smartprod.paperdms.emailimport.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImportRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImportRuleDTO.class);
        ImportRuleDTO importRuleDTO1 = new ImportRuleDTO();
        importRuleDTO1.setId(1L);
        ImportRuleDTO importRuleDTO2 = new ImportRuleDTO();
        assertThat(importRuleDTO1).isNotEqualTo(importRuleDTO2);
        importRuleDTO2.setId(importRuleDTO1.getId());
        assertThat(importRuleDTO1).isEqualTo(importRuleDTO2);
        importRuleDTO2.setId(2L);
        assertThat(importRuleDTO1).isNotEqualTo(importRuleDTO2);
        importRuleDTO1.setId(null);
        assertThat(importRuleDTO1).isNotEqualTo(importRuleDTO2);
    }
}
