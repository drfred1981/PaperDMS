package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTemplateTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTemplate.class);
        DocumentTemplate documentTemplate1 = getDocumentTemplateSample1();
        DocumentTemplate documentTemplate2 = new DocumentTemplate();
        assertThat(documentTemplate1).isNotEqualTo(documentTemplate2);

        documentTemplate2.setId(documentTemplate1.getId());
        assertThat(documentTemplate1).isEqualTo(documentTemplate2);

        documentTemplate2 = getDocumentTemplateSample2();
        assertThat(documentTemplate1).isNotEqualTo(documentTemplate2);
    }

    @Test
    void documentTypeTest() {
        DocumentTemplate documentTemplate = getDocumentTemplateRandomSampleGenerator();
        DocumentType documentTypeBack = getDocumentTypeRandomSampleGenerator();

        documentTemplate.setDocumentType(documentTypeBack);
        assertThat(documentTemplate.getDocumentType()).isEqualTo(documentTypeBack);

        documentTemplate.documentType(null);
        assertThat(documentTemplate.getDocumentType()).isNull();
    }
}
