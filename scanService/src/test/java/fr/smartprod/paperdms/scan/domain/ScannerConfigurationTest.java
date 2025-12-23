package fr.smartprod.paperdms.scan.domain;

import static fr.smartprod.paperdms.scan.domain.ScannerConfigurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScannerConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannerConfiguration.class);
        ScannerConfiguration scannerConfiguration1 = getScannerConfigurationSample1();
        ScannerConfiguration scannerConfiguration2 = new ScannerConfiguration();
        assertThat(scannerConfiguration1).isNotEqualTo(scannerConfiguration2);

        scannerConfiguration2.setId(scannerConfiguration1.getId());
        assertThat(scannerConfiguration1).isEqualTo(scannerConfiguration2);

        scannerConfiguration2 = getScannerConfigurationSample2();
        assertThat(scannerConfiguration1).isNotEqualTo(scannerConfiguration2);
    }
}
