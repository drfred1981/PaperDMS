package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.TagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void tagCategoryTest() {
        Tag tag = getTagRandomSampleGenerator();
        TagCategory tagCategoryBack = getTagCategoryRandomSampleGenerator();

        tag.setTagCategory(tagCategoryBack);
        assertThat(tag.getTagCategory()).isEqualTo(tagCategoryBack);

        tag.tagCategory(null);
        assertThat(tag.getTagCategory()).isNull();
    }
}
