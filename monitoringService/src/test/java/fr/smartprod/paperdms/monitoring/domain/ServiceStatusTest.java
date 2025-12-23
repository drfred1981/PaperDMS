package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.ServiceStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceStatus.class);
        ServiceStatus serviceStatus1 = getServiceStatusSample1();
        ServiceStatus serviceStatus2 = new ServiceStatus();
        assertThat(serviceStatus1).isNotEqualTo(serviceStatus2);

        serviceStatus2.setId(serviceStatus1.getId());
        assertThat(serviceStatus1).isEqualTo(serviceStatus2);

        serviceStatus2 = getServiceStatusSample2();
        assertThat(serviceStatus1).isNotEqualTo(serviceStatus2);
    }
}
