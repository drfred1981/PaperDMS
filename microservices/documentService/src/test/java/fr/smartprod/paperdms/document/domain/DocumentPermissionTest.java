package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentPermissionTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaPermissionGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentPermission.class);
        DocumentPermission documentPermission1 = getDocumentPermissionSample1();
        DocumentPermission documentPermission2 = new DocumentPermission();
        assertThat(documentPermission1).isNotEqualTo(documentPermission2);

        documentPermission2.setId(documentPermission1.getId());
        assertThat(documentPermission1).isEqualTo(documentPermission2);

        documentPermission2 = getDocumentPermissionSample2();
        assertThat(documentPermission1).isNotEqualTo(documentPermission2);
    }

    @Test
    void documentTest() {
        DocumentPermission documentPermission = getDocumentPermissionRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentPermission.setDocument(documentBack);
        assertThat(documentPermission.getDocument()).isEqualTo(documentBack);

        documentPermission.document(null);
        assertThat(documentPermission.getDocument()).isNull();
    }

    @Test
    void metaPermissionGroupTest() {
        DocumentPermission documentPermission = getDocumentPermissionRandomSampleGenerator();
        MetaPermissionGroup metaPermissionGroupBack = getMetaPermissionGroupRandomSampleGenerator();

        documentPermission.setMetaPermissionGroup(metaPermissionGroupBack);
        assertThat(documentPermission.getMetaPermissionGroup()).isEqualTo(metaPermissionGroupBack);

        documentPermission.metaPermissionGroup(null);
        assertThat(documentPermission.getMetaPermissionGroup()).isNull();
    }
}
