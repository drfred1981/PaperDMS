package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTypeFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTypeField.class);
        DocumentTypeField documentTypeField1 = getDocumentTypeFieldSample1();
        DocumentTypeField documentTypeField2 = new DocumentTypeField();
        assertThat(documentTypeField1).isNotEqualTo(documentTypeField2);

        documentTypeField2.setId(documentTypeField1.getId());
        assertThat(documentTypeField1).isEqualTo(documentTypeField2);

        documentTypeField2 = getDocumentTypeFieldSample2();
        assertThat(documentTypeField1).isNotEqualTo(documentTypeField2);
    }

    @Test
    void documentTypeTest() {
        DocumentTypeField documentTypeField = getDocumentTypeFieldRandomSampleGenerator();
        DocumentType documentTypeBack = getDocumentTypeRandomSampleGenerator();

        documentTypeField.setDocumentType(documentTypeBack);
        assertThat(documentTypeField.getDocumentType()).isEqualTo(documentTypeBack);

        documentTypeField.documentType(null);
        assertThat(documentTypeField.getDocumentType()).isNull();
    }
}
