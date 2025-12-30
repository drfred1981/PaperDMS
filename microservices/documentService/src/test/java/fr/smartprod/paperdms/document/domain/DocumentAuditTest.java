package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentAuditTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentAudit.class);
        DocumentAudit documentAudit1 = getDocumentAuditSample1();
        DocumentAudit documentAudit2 = new DocumentAudit();
        assertThat(documentAudit1).isNotEqualTo(documentAudit2);

        documentAudit2.setId(documentAudit1.getId());
        assertThat(documentAudit1).isEqualTo(documentAudit2);

        documentAudit2 = getDocumentAuditSample2();
        assertThat(documentAudit1).isNotEqualTo(documentAudit2);
    }

    @Test
    void documentTest() {
        DocumentAudit documentAudit = getDocumentAuditRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentAudit.setDocument(documentBack);
        assertThat(documentAudit.getDocument()).isEqualTo(documentBack);

        documentAudit.document(null);
        assertThat(documentAudit.getDocument()).isNull();
    }
}
