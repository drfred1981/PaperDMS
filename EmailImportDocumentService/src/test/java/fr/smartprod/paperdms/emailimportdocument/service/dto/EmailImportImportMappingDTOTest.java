package fr.smartprod.paperdms.emailimportdocument.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportImportMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportImportMappingDTO.class);
        EmailImportImportMappingDTO emailImportImportMappingDTO1 = new EmailImportImportMappingDTO();
        emailImportImportMappingDTO1.setId(1L);
        EmailImportImportMappingDTO emailImportImportMappingDTO2 = new EmailImportImportMappingDTO();
        assertThat(emailImportImportMappingDTO1).isNotEqualTo(emailImportImportMappingDTO2);
        emailImportImportMappingDTO2.setId(emailImportImportMappingDTO1.getId());
        assertThat(emailImportImportMappingDTO1).isEqualTo(emailImportImportMappingDTO2);
        emailImportImportMappingDTO2.setId(2L);
        assertThat(emailImportImportMappingDTO1).isNotEqualTo(emailImportImportMappingDTO2);
        emailImportImportMappingDTO1.setId(null);
        assertThat(emailImportImportMappingDTO1).isNotEqualTo(emailImportImportMappingDTO2);
    }
}
