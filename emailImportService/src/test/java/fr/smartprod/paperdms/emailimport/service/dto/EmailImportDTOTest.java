package fr.smartprod.paperdms.emailimport.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailImportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailImportDTO.class);
        EmailImportDTO emailImportDTO1 = new EmailImportDTO();
        emailImportDTO1.setId(1L);
        EmailImportDTO emailImportDTO2 = new EmailImportDTO();
        assertThat(emailImportDTO1).isNotEqualTo(emailImportDTO2);
        emailImportDTO2.setId(emailImportDTO1.getId());
        assertThat(emailImportDTO1).isEqualTo(emailImportDTO2);
        emailImportDTO2.setId(2L);
        assertThat(emailImportDTO1).isNotEqualTo(emailImportDTO2);
        emailImportDTO1.setId(null);
        assertThat(emailImportDTO1).isNotEqualTo(emailImportDTO2);
    }
}
