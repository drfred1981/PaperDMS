package fr.smartprod.paperdms.export.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExportJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportJobDTO.class);
        ExportJobDTO exportJobDTO1 = new ExportJobDTO();
        exportJobDTO1.setId(1L);
        ExportJobDTO exportJobDTO2 = new ExportJobDTO();
        assertThat(exportJobDTO1).isNotEqualTo(exportJobDTO2);
        exportJobDTO2.setId(exportJobDTO1.getId());
        assertThat(exportJobDTO1).isEqualTo(exportJobDTO2);
        exportJobDTO2.setId(2L);
        assertThat(exportJobDTO1).isNotEqualTo(exportJobDTO2);
        exportJobDTO1.setId(null);
        assertThat(exportJobDTO1).isNotEqualTo(exportJobDTO2);
    }
}
