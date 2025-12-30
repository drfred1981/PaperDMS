package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaMetaTagCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaMetaTagCategoryDTO.class);
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO1 = new MetaMetaTagCategoryDTO();
        metaMetaTagCategoryDTO1.setId(1L);
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO2 = new MetaMetaTagCategoryDTO();
        assertThat(metaMetaTagCategoryDTO1).isNotEqualTo(metaMetaTagCategoryDTO2);
        metaMetaTagCategoryDTO2.setId(metaMetaTagCategoryDTO1.getId());
        assertThat(metaMetaTagCategoryDTO1).isEqualTo(metaMetaTagCategoryDTO2);
        metaMetaTagCategoryDTO2.setId(2L);
        assertThat(metaMetaTagCategoryDTO1).isNotEqualTo(metaMetaTagCategoryDTO2);
        metaMetaTagCategoryDTO1.setId(null);
        assertThat(metaMetaTagCategoryDTO1).isNotEqualTo(metaMetaTagCategoryDTO2);
    }
}
