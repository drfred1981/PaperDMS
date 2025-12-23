package fr.smartprod.paperdms.export.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExportPatternDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportPatternDTO.class);
        ExportPatternDTO exportPatternDTO1 = new ExportPatternDTO();
        exportPatternDTO1.setId(1L);
        ExportPatternDTO exportPatternDTO2 = new ExportPatternDTO();
        assertThat(exportPatternDTO1).isNotEqualTo(exportPatternDTO2);
        exportPatternDTO2.setId(exportPatternDTO1.getId());
        assertThat(exportPatternDTO1).isEqualTo(exportPatternDTO2);
        exportPatternDTO2.setId(2L);
        assertThat(exportPatternDTO1).isNotEqualTo(exportPatternDTO2);
        exportPatternDTO1.setId(null);
        assertThat(exportPatternDTO1).isNotEqualTo(exportPatternDTO2);
    }
}
