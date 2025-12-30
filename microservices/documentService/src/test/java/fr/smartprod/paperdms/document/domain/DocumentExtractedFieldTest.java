package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentExtractedFieldTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentExtractedFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentExtractedField.class);
        DocumentExtractedField documentExtractedField1 = getDocumentExtractedFieldSample1();
        DocumentExtractedField documentExtractedField2 = new DocumentExtractedField();
        assertThat(documentExtractedField1).isNotEqualTo(documentExtractedField2);

        documentExtractedField2.setId(documentExtractedField1.getId());
        assertThat(documentExtractedField1).isEqualTo(documentExtractedField2);

        documentExtractedField2 = getDocumentExtractedFieldSample2();
        assertThat(documentExtractedField1).isNotEqualTo(documentExtractedField2);
    }

    @Test
    void documentTest() {
        DocumentExtractedField documentExtractedField = getDocumentExtractedFieldRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentExtractedField.setDocument(documentBack);
        assertThat(documentExtractedField.getDocument()).isEqualTo(documentBack);

        documentExtractedField.document(null);
        assertThat(documentExtractedField.getDocument()).isNull();
    }
}
