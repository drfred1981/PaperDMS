package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaBookmarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaBookmarkDTO.class);
        MetaBookmarkDTO metaBookmarkDTO1 = new MetaBookmarkDTO();
        metaBookmarkDTO1.setId(1L);
        MetaBookmarkDTO metaBookmarkDTO2 = new MetaBookmarkDTO();
        assertThat(metaBookmarkDTO1).isNotEqualTo(metaBookmarkDTO2);
        metaBookmarkDTO2.setId(metaBookmarkDTO1.getId());
        assertThat(metaBookmarkDTO1).isEqualTo(metaBookmarkDTO2);
        metaBookmarkDTO2.setId(2L);
        assertThat(metaBookmarkDTO1).isNotEqualTo(metaBookmarkDTO2);
        metaBookmarkDTO1.setId(null);
        assertThat(metaBookmarkDTO1).isNotEqualTo(metaBookmarkDTO2);
    }
}
