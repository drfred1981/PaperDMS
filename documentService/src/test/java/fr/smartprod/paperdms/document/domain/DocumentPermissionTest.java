package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentPermissionTestSamples.*;
import static fr.smartprod.paperdms.document.domain.PermissionGroupTestSamples.*;
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
    void permissionGroupTest() {
        DocumentPermission documentPermission = getDocumentPermissionRandomSampleGenerator();
        PermissionGroup permissionGroupBack = getPermissionGroupRandomSampleGenerator();

        documentPermission.setPermissionGroup(permissionGroupBack);
        assertThat(documentPermission.getPermissionGroup()).isEqualTo(permissionGroupBack);

        documentPermission.permissionGroup(null);
        assertThat(documentPermission.getPermissionGroup()).isNull();
    }
}
