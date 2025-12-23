package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmartFolderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartFolderDTO.class);
        SmartFolderDTO smartFolderDTO1 = new SmartFolderDTO();
        smartFolderDTO1.setId(1L);
        SmartFolderDTO smartFolderDTO2 = new SmartFolderDTO();
        assertThat(smartFolderDTO1).isNotEqualTo(smartFolderDTO2);
        smartFolderDTO2.setId(smartFolderDTO1.getId());
        assertThat(smartFolderDTO1).isEqualTo(smartFolderDTO2);
        smartFolderDTO2.setId(2L);
        assertThat(smartFolderDTO1).isNotEqualTo(smartFolderDTO2);
        smartFolderDTO1.setId(null);
        assertThat(smartFolderDTO1).isNotEqualTo(smartFolderDTO2);
    }
}
