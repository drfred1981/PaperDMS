package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;
import static fr.smartprod.paperdms.document.domain.FolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void folderTest() {
        Document document = getDocumentRandomSampleGenerator();
        Folder folderBack = getFolderRandomSampleGenerator();

        document.setFolder(folderBack);
        assertThat(document.getFolder()).isEqualTo(folderBack);

        document.folder(null);
        assertThat(document.getFolder()).isNull();
    }

    @Test
    void documentTypeTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentType documentTypeBack = getDocumentTypeRandomSampleGenerator();

        document.setDocumentType(documentTypeBack);
        assertThat(document.getDocumentType()).isEqualTo(documentTypeBack);

        document.documentType(null);
        assertThat(document.getDocumentType()).isNull();
    }
}
