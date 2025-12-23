package fr.smartprod.paperdms.scan.domain;

import static fr.smartprod.paperdms.scan.domain.ScanBatchTestSamples.*;
import static fr.smartprod.paperdms.scan.domain.ScanJobTestSamples.*;
import static fr.smartprod.paperdms.scan.domain.ScannerConfigurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScanJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScanJob.class);
        ScanJob scanJob1 = getScanJobSample1();
        ScanJob scanJob2 = new ScanJob();
        assertThat(scanJob1).isNotEqualTo(scanJob2);

        scanJob2.setId(scanJob1.getId());
        assertThat(scanJob1).isEqualTo(scanJob2);

        scanJob2 = getScanJobSample2();
        assertThat(scanJob1).isNotEqualTo(scanJob2);
    }

    @Test
    void scannerConfigTest() {
        ScanJob scanJob = getScanJobRandomSampleGenerator();
        ScannerConfiguration scannerConfigurationBack = getScannerConfigurationRandomSampleGenerator();

        scanJob.setScannerConfig(scannerConfigurationBack);
        assertThat(scanJob.getScannerConfig()).isEqualTo(scannerConfigurationBack);

        scanJob.scannerConfig(null);
        assertThat(scanJob.getScannerConfig()).isNull();
    }

    @Test
    void batchTest() {
        ScanJob scanJob = getScanJobRandomSampleGenerator();
        ScanBatch scanBatchBack = getScanBatchRandomSampleGenerator();

        scanJob.setBatch(scanBatchBack);
        assertThat(scanJob.getBatch()).isEqualTo(scanBatchBack);

        scanJob.batch(null);
        assertThat(scanJob.getBatch()).isNull();
    }
}
