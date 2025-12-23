package fr.smartprod.paperdms.scan.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScannerConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannerConfigurationDTO.class);
        ScannerConfigurationDTO scannerConfigurationDTO1 = new ScannerConfigurationDTO();
        scannerConfigurationDTO1.setId(1L);
        ScannerConfigurationDTO scannerConfigurationDTO2 = new ScannerConfigurationDTO();
        assertThat(scannerConfigurationDTO1).isNotEqualTo(scannerConfigurationDTO2);
        scannerConfigurationDTO2.setId(scannerConfigurationDTO1.getId());
        assertThat(scannerConfigurationDTO1).isEqualTo(scannerConfigurationDTO2);
        scannerConfigurationDTO2.setId(2L);
        assertThat(scannerConfigurationDTO1).isNotEqualTo(scannerConfigurationDTO2);
        scannerConfigurationDTO1.setId(null);
        assertThat(scannerConfigurationDTO1).isNotEqualTo(scannerConfigurationDTO2);
    }
}
