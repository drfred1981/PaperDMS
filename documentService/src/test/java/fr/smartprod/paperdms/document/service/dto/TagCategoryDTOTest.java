package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCategoryDTO.class);
        TagCategoryDTO tagCategoryDTO1 = new TagCategoryDTO();
        tagCategoryDTO1.setId(1L);
        TagCategoryDTO tagCategoryDTO2 = new TagCategoryDTO();
        assertThat(tagCategoryDTO1).isNotEqualTo(tagCategoryDTO2);
        tagCategoryDTO2.setId(tagCategoryDTO1.getId());
        assertThat(tagCategoryDTO1).isEqualTo(tagCategoryDTO2);
        tagCategoryDTO2.setId(2L);
        assertThat(tagCategoryDTO1).isNotEqualTo(tagCategoryDTO2);
        tagCategoryDTO1.setId(null);
        assertThat(tagCategoryDTO1).isNotEqualTo(tagCategoryDTO2);
    }
}
