package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.AlertRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertRule.class);
        AlertRule alertRule1 = getAlertRuleSample1();
        AlertRule alertRule2 = new AlertRule();
        assertThat(alertRule1).isNotEqualTo(alertRule2);

        alertRule2.setId(alertRule1.getId());
        assertThat(alertRule1).isEqualTo(alertRule2);

        alertRule2 = getAlertRuleSample2();
        assertThat(alertRule1).isNotEqualTo(alertRule2);
    }
}
