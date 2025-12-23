package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.TagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.TagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCategory.class);
        TagCategory tagCategory1 = getTagCategorySample1();
        TagCategory tagCategory2 = new TagCategory();
        assertThat(tagCategory1).isNotEqualTo(tagCategory2);

        tagCategory2.setId(tagCategory1.getId());
        assertThat(tagCategory1).isEqualTo(tagCategory2);

        tagCategory2 = getTagCategorySample2();
        assertThat(tagCategory1).isNotEqualTo(tagCategory2);
    }

    @Test
    void childrenTest() {
        TagCategory tagCategory = getTagCategoryRandomSampleGenerator();
        TagCategory tagCategoryBack = getTagCategoryRandomSampleGenerator();

        tagCategory.addChildren(tagCategoryBack);
        assertThat(tagCategory.getChildren()).containsOnly(tagCategoryBack);
        assertThat(tagCategoryBack.getParent()).isEqualTo(tagCategory);

        tagCategory.removeChildren(tagCategoryBack);
        assertThat(tagCategory.getChildren()).doesNotContain(tagCategoryBack);
        assertThat(tagCategoryBack.getParent()).isNull();

        tagCategory.children(new HashSet<>(Set.of(tagCategoryBack)));
        assertThat(tagCategory.getChildren()).containsOnly(tagCategoryBack);
        assertThat(tagCategoryBack.getParent()).isEqualTo(tagCategory);

        tagCategory.setChildren(new HashSet<>());
        assertThat(tagCategory.getChildren()).doesNotContain(tagCategoryBack);
        assertThat(tagCategoryBack.getParent()).isNull();
    }

    @Test
    void tagsTest() {
        TagCategory tagCategory = getTagCategoryRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        tagCategory.addTags(tagBack);
        assertThat(tagCategory.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getTagCategory()).isEqualTo(tagCategory);

        tagCategory.removeTags(tagBack);
        assertThat(tagCategory.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getTagCategory()).isNull();

        tagCategory.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(tagCategory.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getTagCategory()).isEqualTo(tagCategory);

        tagCategory.setTags(new HashSet<>());
        assertThat(tagCategory.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getTagCategory()).isNull();
    }

    @Test
    void parentTest() {
        TagCategory tagCategory = getTagCategoryRandomSampleGenerator();
        TagCategory tagCategoryBack = getTagCategoryRandomSampleGenerator();

        tagCategory.setParent(tagCategoryBack);
        assertThat(tagCategory.getParent()).isEqualTo(tagCategoryBack);

        tagCategory.parent(null);
        assertThat(tagCategory.getParent()).isNull();
    }
}
