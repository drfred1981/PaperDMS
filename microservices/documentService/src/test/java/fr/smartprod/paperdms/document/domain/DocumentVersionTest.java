package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentVersionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentVersion.class);
        DocumentVersion documentVersion1 = getDocumentVersionSample1();
        DocumentVersion documentVersion2 = new DocumentVersion();
        assertThat(documentVersion1).isNotEqualTo(documentVersion2);

        documentVersion2.setId(documentVersion1.getId());
        assertThat(documentVersion1).isEqualTo(documentVersion2);

        documentVersion2 = getDocumentVersionSample2();
        assertThat(documentVersion1).isNotEqualTo(documentVersion2);
    }

    @Test
    void documentTest() {
        DocumentVersion documentVersion = getDocumentVersionRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentVersion.setDocument(documentBack);
        assertThat(documentVersion.getDocument()).isEqualTo(documentBack);

        documentVersion.document(null);
        assertThat(documentVersion.getDocument()).isNull();
    }
}
