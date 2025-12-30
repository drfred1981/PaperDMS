package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitoringAlertRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitoringAlertRuleDTO.class);
        MonitoringAlertRuleDTO monitoringAlertRuleDTO1 = new MonitoringAlertRuleDTO();
        monitoringAlertRuleDTO1.setId(1L);
        MonitoringAlertRuleDTO monitoringAlertRuleDTO2 = new MonitoringAlertRuleDTO();
        assertThat(monitoringAlertRuleDTO1).isNotEqualTo(monitoringAlertRuleDTO2);
        monitoringAlertRuleDTO2.setId(monitoringAlertRuleDTO1.getId());
        assertThat(monitoringAlertRuleDTO1).isEqualTo(monitoringAlertRuleDTO2);
        monitoringAlertRuleDTO2.setId(2L);
        assertThat(monitoringAlertRuleDTO1).isNotEqualTo(monitoringAlertRuleDTO2);
        monitoringAlertRuleDTO1.setId(null);
        assertThat(monitoringAlertRuleDTO1).isNotEqualTo(monitoringAlertRuleDTO2);
    }
}
