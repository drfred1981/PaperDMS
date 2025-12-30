package fr.smartprod.paperdms.export.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExportResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportResultDTO.class);
        ExportResultDTO exportResultDTO1 = new ExportResultDTO();
        exportResultDTO1.setId(1L);
        ExportResultDTO exportResultDTO2 = new ExportResultDTO();
        assertThat(exportResultDTO1).isNotEqualTo(exportResultDTO2);
        exportResultDTO2.setId(exportResultDTO1.getId());
        assertThat(exportResultDTO1).isEqualTo(exportResultDTO2);
        exportResultDTO2.setId(2L);
        assertThat(exportResultDTO1).isNotEqualTo(exportResultDTO2);
        exportResultDTO1.setId(null);
        assertThat(exportResultDTO1).isNotEqualTo(exportResultDTO2);
    }
}
