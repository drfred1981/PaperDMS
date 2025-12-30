package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaSmartFolderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaSmartFolderDTO.class);
        MetaSmartFolderDTO metaSmartFolderDTO1 = new MetaSmartFolderDTO();
        metaSmartFolderDTO1.setId(1L);
        MetaSmartFolderDTO metaSmartFolderDTO2 = new MetaSmartFolderDTO();
        assertThat(metaSmartFolderDTO1).isNotEqualTo(metaSmartFolderDTO2);
        metaSmartFolderDTO2.setId(metaSmartFolderDTO1.getId());
        assertThat(metaSmartFolderDTO1).isEqualTo(metaSmartFolderDTO2);
        metaSmartFolderDTO2.setId(2L);
        assertThat(metaSmartFolderDTO1).isNotEqualTo(metaSmartFolderDTO2);
        metaSmartFolderDTO1.setId(null);
        assertThat(metaSmartFolderDTO1).isNotEqualTo(metaSmartFolderDTO2);
    }
}
