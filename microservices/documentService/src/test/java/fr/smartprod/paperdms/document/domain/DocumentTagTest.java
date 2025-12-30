package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTagTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaTagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTag.class);
        DocumentTag documentTag1 = getDocumentTagSample1();
        DocumentTag documentTag2 = new DocumentTag();
        assertThat(documentTag1).isNotEqualTo(documentTag2);

        documentTag2.setId(documentTag1.getId());
        assertThat(documentTag1).isEqualTo(documentTag2);

        documentTag2 = getDocumentTagSample2();
        assertThat(documentTag1).isNotEqualTo(documentTag2);
    }

    @Test
    void documentTest() {
        DocumentTag documentTag = getDocumentTagRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentTag.setDocument(documentBack);
        assertThat(documentTag.getDocument()).isEqualTo(documentBack);

        documentTag.document(null);
        assertThat(documentTag.getDocument()).isNull();
    }

    @Test
    void metaTagTest() {
        DocumentTag documentTag = getDocumentTagRandomSampleGenerator();
        MetaTag metaTagBack = getMetaTagRandomSampleGenerator();

        documentTag.setMetaTag(metaTagBack);
        assertThat(documentTag.getMetaTag()).isEqualTo(metaTagBack);

        documentTag.metaTag(null);
        assertThat(documentTag.getMetaTag()).isNull();
    }
}
