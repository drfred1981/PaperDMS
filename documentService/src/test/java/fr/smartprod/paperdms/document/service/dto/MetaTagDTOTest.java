package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaTagDTO.class);
        MetaTagDTO metaTagDTO1 = new MetaTagDTO();
        metaTagDTO1.setId(1L);
        MetaTagDTO metaTagDTO2 = new MetaTagDTO();
        assertThat(metaTagDTO1).isNotEqualTo(metaTagDTO2);
        metaTagDTO2.setId(metaTagDTO1.getId());
        assertThat(metaTagDTO1).isEqualTo(metaTagDTO2);
        metaTagDTO2.setId(2L);
        assertThat(metaTagDTO1).isNotEqualTo(metaTagDTO2);
        metaTagDTO1.setId(null);
        assertThat(metaTagDTO1).isNotEqualTo(metaTagDTO2);
    }
}
