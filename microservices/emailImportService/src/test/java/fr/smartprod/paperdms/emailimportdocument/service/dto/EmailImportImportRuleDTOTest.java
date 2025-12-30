package fr.smartprod.paperdms.emailimportdocument.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportImportRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportImportRuleDTO.class);
        EmailImportImportRuleDTO emailImportImportRuleDTO1 = new EmailImportImportRuleDTO();
        emailImportImportRuleDTO1.setId(1L);
        EmailImportImportRuleDTO emailImportImportRuleDTO2 = new EmailImportImportRuleDTO();
        assertThat(emailImportImportRuleDTO1).isNotEqualTo(emailImportImportRuleDTO2);
        emailImportImportRuleDTO2.setId(emailImportImportRuleDTO1.getId());
        assertThat(emailImportImportRuleDTO1).isEqualTo(emailImportImportRuleDTO2);
        emailImportImportRuleDTO2.setId(2L);
        assertThat(emailImportImportRuleDTO1).isNotEqualTo(emailImportImportRuleDTO2);
        emailImportImportRuleDTO1.setId(null);
        assertThat(emailImportImportRuleDTO1).isNotEqualTo(emailImportImportRuleDTO2);
    }
}
