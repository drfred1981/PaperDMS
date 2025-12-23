package fr.smartprod.paperdms.scan.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScanBatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScanBatchDTO.class);
        ScanBatchDTO scanBatchDTO1 = new ScanBatchDTO();
        scanBatchDTO1.setId(1L);
        ScanBatchDTO scanBatchDTO2 = new ScanBatchDTO();
        assertThat(scanBatchDTO1).isNotEqualTo(scanBatchDTO2);
        scanBatchDTO2.setId(scanBatchDTO1.getId());
        assertThat(scanBatchDTO1).isEqualTo(scanBatchDTO2);
        scanBatchDTO2.setId(2L);
        assertThat(scanBatchDTO1).isNotEqualTo(scanBatchDTO2);
        scanBatchDTO1.setId(null);
        assertThat(scanBatchDTO1).isNotEqualTo(scanBatchDTO2);
    }
}
