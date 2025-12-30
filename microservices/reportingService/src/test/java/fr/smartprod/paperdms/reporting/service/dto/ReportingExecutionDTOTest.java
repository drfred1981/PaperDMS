package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingExecutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingExecutionDTO.class);
        ReportingExecutionDTO reportingExecutionDTO1 = new ReportingExecutionDTO();
        reportingExecutionDTO1.setId(1L);
        ReportingExecutionDTO reportingExecutionDTO2 = new ReportingExecutionDTO();
        assertThat(reportingExecutionDTO1).isNotEqualTo(reportingExecutionDTO2);
        reportingExecutionDTO2.setId(reportingExecutionDTO1.getId());
        assertThat(reportingExecutionDTO1).isEqualTo(reportingExecutionDTO2);
        reportingExecutionDTO2.setId(2L);
        assertThat(reportingExecutionDTO1).isNotEqualTo(reportingExecutionDTO2);
        reportingExecutionDTO1.setId(null);
        assertThat(reportingExecutionDTO1).isNotEqualTo(reportingExecutionDTO2);
    }
}
