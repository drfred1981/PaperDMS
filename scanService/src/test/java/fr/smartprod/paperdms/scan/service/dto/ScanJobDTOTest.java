package fr.smartprod.paperdms.scan.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScanJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScanJobDTO.class);
        ScanJobDTO scanJobDTO1 = new ScanJobDTO();
        scanJobDTO1.setId(1L);
        ScanJobDTO scanJobDTO2 = new ScanJobDTO();
        assertThat(scanJobDTO1).isNotEqualTo(scanJobDTO2);
        scanJobDTO2.setId(scanJobDTO1.getId());
        assertThat(scanJobDTO1).isEqualTo(scanJobDTO2);
        scanJobDTO2.setId(2L);
        assertThat(scanJobDTO1).isNotEqualTo(scanJobDTO2);
        scanJobDTO1.setId(null);
        assertThat(scanJobDTO1).isNotEqualTo(scanJobDTO2);
    }
}
