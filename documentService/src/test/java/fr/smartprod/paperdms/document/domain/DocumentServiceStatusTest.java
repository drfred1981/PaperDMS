package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentServiceStatusTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentServiceStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentServiceStatus.class);
        DocumentServiceStatus documentServiceStatus1 = getDocumentServiceStatusSample1();
        DocumentServiceStatus documentServiceStatus2 = new DocumentServiceStatus();
        assertThat(documentServiceStatus1).isNotEqualTo(documentServiceStatus2);

        documentServiceStatus2.setId(documentServiceStatus1.getId());
        assertThat(documentServiceStatus1).isEqualTo(documentServiceStatus2);

        documentServiceStatus2 = getDocumentServiceStatusSample2();
        assertThat(documentServiceStatus1).isNotEqualTo(documentServiceStatus2);
    }

    @Test
    void documentTest() {
        DocumentServiceStatus documentServiceStatus = getDocumentServiceStatusRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentServiceStatus.setDocument(documentBack);
        assertThat(documentServiceStatus.getDocument()).isEqualTo(documentBack);

        documentServiceStatus.document(null);
        assertThat(documentServiceStatus.getDocument()).isNull();
    }
}
