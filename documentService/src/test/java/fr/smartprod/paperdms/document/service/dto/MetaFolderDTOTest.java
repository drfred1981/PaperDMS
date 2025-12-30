package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaFolderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaFolderDTO.class);
        MetaFolderDTO metaFolderDTO1 = new MetaFolderDTO();
        metaFolderDTO1.setId(1L);
        MetaFolderDTO metaFolderDTO2 = new MetaFolderDTO();
        assertThat(metaFolderDTO1).isNotEqualTo(metaFolderDTO2);
        metaFolderDTO2.setId(metaFolderDTO1.getId());
        assertThat(metaFolderDTO1).isEqualTo(metaFolderDTO2);
        metaFolderDTO2.setId(2L);
        assertThat(metaFolderDTO1).isNotEqualTo(metaFolderDTO2);
        metaFolderDTO1.setId(null);
        assertThat(metaFolderDTO1).isNotEqualTo(metaFolderDTO2);
    }
}
