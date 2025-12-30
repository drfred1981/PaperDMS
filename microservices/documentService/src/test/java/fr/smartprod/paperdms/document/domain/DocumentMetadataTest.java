package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentMetadataTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentMetadata.class);
        DocumentMetadata documentMetadata1 = getDocumentMetadataSample1();
        DocumentMetadata documentMetadata2 = new DocumentMetadata();
        assertThat(documentMetadata1).isNotEqualTo(documentMetadata2);

        documentMetadata2.setId(documentMetadata1.getId());
        assertThat(documentMetadata1).isEqualTo(documentMetadata2);

        documentMetadata2 = getDocumentMetadataSample2();
        assertThat(documentMetadata1).isNotEqualTo(documentMetadata2);
    }

    @Test
    void documentTest() {
        DocumentMetadata documentMetadata = getDocumentMetadataRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentMetadata.setDocument(documentBack);
        assertThat(documentMetadata.getDocument()).isEqualTo(documentBack);

        documentMetadata.document(null);
        assertThat(documentMetadata.getDocument()).isNull();
    }
}
