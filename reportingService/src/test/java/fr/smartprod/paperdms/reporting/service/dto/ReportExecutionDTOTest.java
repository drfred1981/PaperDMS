package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportExecutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportExecutionDTO.class);
        ReportExecutionDTO reportExecutionDTO1 = new ReportExecutionDTO();
        reportExecutionDTO1.setId(1L);
        ReportExecutionDTO reportExecutionDTO2 = new ReportExecutionDTO();
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
        reportExecutionDTO2.setId(reportExecutionDTO1.getId());
        assertThat(reportExecutionDTO1).isEqualTo(reportExecutionDTO2);
        reportExecutionDTO2.setId(2L);
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
        reportExecutionDTO1.setId(null);
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
    }
}
