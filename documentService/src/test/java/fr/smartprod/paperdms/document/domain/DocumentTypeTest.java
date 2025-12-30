package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTemplateTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentType.class);
        DocumentType documentType1 = getDocumentTypeSample1();
        DocumentType documentType2 = new DocumentType();
        assertThat(documentType1).isNotEqualTo(documentType2);

        documentType2.setId(documentType1.getId());
        assertThat(documentType1).isEqualTo(documentType2);

        documentType2 = getDocumentTypeSample2();
        assertThat(documentType1).isNotEqualTo(documentType2);
    }

    @Test
    void documentsTest() {
        DocumentType documentType = getDocumentTypeRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentType.addDocuments(documentBack);
        assertThat(documentType.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getDocumentType()).isEqualTo(documentType);

        documentType.removeDocuments(documentBack);
        assertThat(documentType.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getDocumentType()).isNull();

        documentType.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(documentType.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getDocumentType()).isEqualTo(documentType);

        documentType.setDocuments(new HashSet<>());
        assertThat(documentType.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getDocumentType()).isNull();
    }

    @Test
    void documentTemplatesTest() {
        DocumentType documentType = getDocumentTypeRandomSampleGenerator();
        DocumentTemplate documentTemplateBack = getDocumentTemplateRandomSampleGenerator();

        documentType.addDocumentTemplates(documentTemplateBack);
        assertThat(documentType.getDocumentTemplates()).containsOnly(documentTemplateBack);
        assertThat(documentTemplateBack.getDocumentType()).isEqualTo(documentType);

        documentType.removeDocumentTemplates(documentTemplateBack);
        assertThat(documentType.getDocumentTemplates()).doesNotContain(documentTemplateBack);
        assertThat(documentTemplateBack.getDocumentType()).isNull();

        documentType.documentTemplates(new HashSet<>(Set.of(documentTemplateBack)));
        assertThat(documentType.getDocumentTemplates()).containsOnly(documentTemplateBack);
        assertThat(documentTemplateBack.getDocumentType()).isEqualTo(documentType);

        documentType.setDocumentTemplates(new HashSet<>());
        assertThat(documentType.getDocumentTemplates()).doesNotContain(documentTemplateBack);
        assertThat(documentTemplateBack.getDocumentType()).isNull();
    }

    @Test
    void fieldsTest() {
        DocumentType documentType = getDocumentTypeRandomSampleGenerator();
        DocumentTypeField documentTypeFieldBack = getDocumentTypeFieldRandomSampleGenerator();

        documentType.addFields(documentTypeFieldBack);
        assertThat(documentType.getFields()).containsOnly(documentTypeFieldBack);
        assertThat(documentTypeFieldBack.getDocumentType()).isEqualTo(documentType);

        documentType.removeFields(documentTypeFieldBack);
        assertThat(documentType.getFields()).doesNotContain(documentTypeFieldBack);
        assertThat(documentTypeFieldBack.getDocumentType()).isNull();

        documentType.fields(new HashSet<>(Set.of(documentTypeFieldBack)));
        assertThat(documentType.getFields()).containsOnly(documentTypeFieldBack);
        assertThat(documentTypeFieldBack.getDocumentType()).isEqualTo(documentType);

        documentType.setFields(new HashSet<>());
        assertThat(documentType.getFields()).doesNotContain(documentTypeFieldBack);
        assertThat(documentTypeFieldBack.getDocumentType()).isNull();
    }
}
