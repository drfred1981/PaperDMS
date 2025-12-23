package fr.smartprod.paperdms.scan.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.scan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScannedPageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannedPageDTO.class);
        ScannedPageDTO scannedPageDTO1 = new ScannedPageDTO();
        scannedPageDTO1.setId(1L);
        ScannedPageDTO scannedPageDTO2 = new ScannedPageDTO();
        assertThat(scannedPageDTO1).isNotEqualTo(scannedPageDTO2);
        scannedPageDTO2.setId(scannedPageDTO1.getId());
        assertThat(scannedPageDTO1).isEqualTo(scannedPageDTO2);
        scannedPageDTO2.setId(2L);
        assertThat(scannedPageDTO1).isNotEqualTo(scannedPageDTO2);
        scannedPageDTO1.setId(null);
        assertThat(scannedPageDTO1).isNotEqualTo(scannedPageDTO2);
    }
}
