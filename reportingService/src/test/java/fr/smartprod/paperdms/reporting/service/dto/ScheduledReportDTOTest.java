package fr.smartprod.paperdms.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduledReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledReportDTO.class);
        ScheduledReportDTO scheduledReportDTO1 = new ScheduledReportDTO();
        scheduledReportDTO1.setId(1L);
        ScheduledReportDTO scheduledReportDTO2 = new ScheduledReportDTO();
        assertThat(scheduledReportDTO1).isNotEqualTo(scheduledReportDTO2);
        scheduledReportDTO2.setId(scheduledReportDTO1.getId());
        assertThat(scheduledReportDTO1).isEqualTo(scheduledReportDTO2);
        scheduledReportDTO2.setId(2L);
        assertThat(scheduledReportDTO1).isNotEqualTo(scheduledReportDTO2);
        scheduledReportDTO1.setId(null);
        assertThat(scheduledReportDTO1).isNotEqualTo(scheduledReportDTO2);
    }
}
