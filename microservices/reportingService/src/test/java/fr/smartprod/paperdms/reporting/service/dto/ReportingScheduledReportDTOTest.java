package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingScheduledReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingScheduledReportDTO.class);
        ReportingScheduledReportDTO reportingScheduledReportDTO1 = new ReportingScheduledReportDTO();
        reportingScheduledReportDTO1.setId(1L);
        ReportingScheduledReportDTO reportingScheduledReportDTO2 = new ReportingScheduledReportDTO();
        assertThat(reportingScheduledReportDTO1).isNotEqualTo(reportingScheduledReportDTO2);
        reportingScheduledReportDTO2.setId(reportingScheduledReportDTO1.getId());
        assertThat(reportingScheduledReportDTO1).isEqualTo(reportingScheduledReportDTO2);
        reportingScheduledReportDTO2.setId(2L);
        assertThat(reportingScheduledReportDTO1).isNotEqualTo(reportingScheduledReportDTO2);
        reportingScheduledReportDTO1.setId(null);
        assertThat(reportingScheduledReportDTO1).isNotEqualTo(reportingScheduledReportDTO2);
    }
}
