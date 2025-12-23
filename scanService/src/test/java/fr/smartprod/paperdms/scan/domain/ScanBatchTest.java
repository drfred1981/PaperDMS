package fr.smartprod.paperdms.scan.domain;

import static fr.smartprod.paperdms.scan.domain.ScanBatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScanBatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScanBatch.class);
        ScanBatch scanBatch1 = getScanBatchSample1();
        ScanBatch scanBatch2 = new ScanBatch();
        assertThat(scanBatch1).isNotEqualTo(scanBatch2);

        scanBatch2.setId(scanBatch1.getId());
        assertThat(scanBatch1).isEqualTo(scanBatch2);

        scanBatch2 = getScanBatchSample2();
        assertThat(scanBatch1).isNotEqualTo(scanBatch2);
    }
}
