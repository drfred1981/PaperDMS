package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaTagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MetaMetaTagCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaMetaTagCategory.class);
        MetaMetaTagCategory metaMetaTagCategory1 = getMetaMetaTagCategorySample1();
        MetaMetaTagCategory metaMetaTagCategory2 = new MetaMetaTagCategory();
        assertThat(metaMetaTagCategory1).isNotEqualTo(metaMetaTagCategory2);

        metaMetaTagCategory2.setId(metaMetaTagCategory1.getId());
        assertThat(metaMetaTagCategory1).isEqualTo(metaMetaTagCategory2);

        metaMetaTagCategory2 = getMetaMetaTagCategorySample2();
        assertThat(metaMetaTagCategory1).isNotEqualTo(metaMetaTagCategory2);
    }

    @Test
    void childrenTest() {
        MetaMetaTagCategory metaMetaTagCategory = getMetaMetaTagCategoryRandomSampleGenerator();
        MetaMetaTagCategory metaMetaTagCategoryBack = getMetaMetaTagCategoryRandomSampleGenerator();

        metaMetaTagCategory.addChildren(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategory.getChildren()).containsOnly(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategoryBack.getParent()).isEqualTo(metaMetaTagCategory);

        metaMetaTagCategory.removeChildren(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategory.getChildren()).doesNotContain(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategoryBack.getParent()).isNull();

        metaMetaTagCategory.children(new HashSet<>(Set.of(metaMetaTagCategoryBack)));
        assertThat(metaMetaTagCategory.getChildren()).containsOnly(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategoryBack.getParent()).isEqualTo(metaMetaTagCategory);

        metaMetaTagCategory.setChildren(new HashSet<>());
        assertThat(metaMetaTagCategory.getChildren()).doesNotContain(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategoryBack.getParent()).isNull();
    }

    @Test
    void metaTagsTest() {
        MetaMetaTagCategory metaMetaTagCategory = getMetaMetaTagCategoryRandomSampleGenerator();
        MetaTag metaTagBack = getMetaTagRandomSampleGenerator();

        metaMetaTagCategory.addMetaTags(metaTagBack);
        assertThat(metaMetaTagCategory.getMetaTags()).containsOnly(metaTagBack);
        assertThat(metaTagBack.getMetaMetaTagCategory()).isEqualTo(metaMetaTagCategory);

        metaMetaTagCategory.removeMetaTags(metaTagBack);
        assertThat(metaMetaTagCategory.getMetaTags()).doesNotContain(metaTagBack);
        assertThat(metaTagBack.getMetaMetaTagCategory()).isNull();

        metaMetaTagCategory.metaTags(new HashSet<>(Set.of(metaTagBack)));
        assertThat(metaMetaTagCategory.getMetaTags()).containsOnly(metaTagBack);
        assertThat(metaTagBack.getMetaMetaTagCategory()).isEqualTo(metaMetaTagCategory);

        metaMetaTagCategory.setMetaTags(new HashSet<>());
        assertThat(metaMetaTagCategory.getMetaTags()).doesNotContain(metaTagBack);
        assertThat(metaTagBack.getMetaMetaTagCategory()).isNull();
    }

    @Test
    void parentTest() {
        MetaMetaTagCategory metaMetaTagCategory = getMetaMetaTagCategoryRandomSampleGenerator();
        MetaMetaTagCategory metaMetaTagCategoryBack = getMetaMetaTagCategoryRandomSampleGenerator();

        metaMetaTagCategory.setParent(metaMetaTagCategoryBack);
        assertThat(metaMetaTagCategory.getParent()).isEqualTo(metaMetaTagCategoryBack);

        metaMetaTagCategory.parent(null);
        assertThat(metaMetaTagCategory.getParent()).isNull();
    }
}
