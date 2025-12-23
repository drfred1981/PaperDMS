package fr.smartprod.paperdms.scan.domain;

import static fr.smartprod.paperdms.scan.domain.ScanJobTestSamples.*;
import static fr.smartprod.paperdms.scan.domain.ScannedPageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScannedPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannedPage.class);
        ScannedPage scannedPage1 = getScannedPageSample1();
        ScannedPage scannedPage2 = new ScannedPage();
        assertThat(scannedPage1).isNotEqualTo(scannedPage2);

        scannedPage2.setId(scannedPage1.getId());
        assertThat(scannedPage1).isEqualTo(scannedPage2);

        scannedPage2 = getScannedPageSample2();
        assertThat(scannedPage1).isNotEqualTo(scannedPage2);
    }

    @Test
    void scanJobTest() {
        ScannedPage scannedPage = getScannedPageRandomSampleGenerator();
        ScanJob scanJobBack = getScanJobRandomSampleGenerator();

        scannedPage.setScanJob(scanJobBack);
        assertThat(scannedPage.getScanJob()).isEqualTo(scanJobBack);

        scannedPage.scanJob(null);
        assertThat(scannedPage.getScanJob()).isNull();
    }
}
