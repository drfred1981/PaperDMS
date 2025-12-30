package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTagTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaTagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MetaTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaTag.class);
        MetaTag metaTag1 = getMetaTagSample1();
        MetaTag metaTag2 = new MetaTag();
        assertThat(metaTag1).isNotEqualTo(metaTag2);

        metaTag2.setId(metaTag1.getId());
        assertThat(metaTag1).isEqualTo(metaTag2);

        metaTag2 = getMetaTagSample2();
        assertThat(metaTag1).isNotEqualTo(metaTag2);
    }

    @Test
    void documentTagsTest() {
        MetaTag metaTag = getMetaTagRandomSampleGenerator();
        DocumentTag documentTagBack = getDocumentTagRandomSampleGenerator();

        metaTag.addDocumentTags(documentTagBack);
        assertThat(metaTag.getDocumentTags()).containsOnly(documentTagBack);
        assertThat(documentTagBack.getMetaTag()).isEqualTo(metaTag);

        metaTag.removeDocumentTags(documentTagBack);
        assertThat(metaTag.getDocumentTags()).doesNotContain(documentTagBack);
        assertThat(documentTagBack.getMetaTag()).isNull();

        metaTag.documentTags(new HashSet<>(Set.of(documentTagBack)));
        assertThat(metaTag.getDocumentTags()).containsOnly(documentTagBack);
        assertThat(documentTagBack.getMetaTag()).isEqualTo(metaTag);

        metaTag.setDocumentTags(new HashSet<>());
        assertThat(metaTag.getDocumentTags()).doesNotContain(documentTagBack);
        assertThat(documentTagBack.getMetaTag()).isNull();
    }

    @Test
    void metaMetaTagCategoryTest() {
        MetaTag metaTag = getMetaTagRandomSampleGenerator();
        MetaMetaTagCategory metaMetaTagCategoryBack = getMetaMetaTagCategoryRandomSampleGenerator();

        metaTag.setMetaMetaTagCategory(metaMetaTagCategoryBack);
        assertThat(metaTag.getMetaMetaTagCategory()).isEqualTo(metaMetaTagCategoryBack);

        metaTag.metaMetaTagCategory(null);
        assertThat(metaTag.getMetaMetaTagCategory()).isNull();
    }
}
