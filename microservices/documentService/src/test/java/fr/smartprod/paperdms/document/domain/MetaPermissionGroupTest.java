package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentPermissionTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaPermissionGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MetaPermissionGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaPermissionGroup.class);
        MetaPermissionGroup metaPermissionGroup1 = getMetaPermissionGroupSample1();
        MetaPermissionGroup metaPermissionGroup2 = new MetaPermissionGroup();
        assertThat(metaPermissionGroup1).isNotEqualTo(metaPermissionGroup2);

        metaPermissionGroup2.setId(metaPermissionGroup1.getId());
        assertThat(metaPermissionGroup1).isEqualTo(metaPermissionGroup2);

        metaPermissionGroup2 = getMetaPermissionGroupSample2();
        assertThat(metaPermissionGroup1).isNotEqualTo(metaPermissionGroup2);
    }

    @Test
    void documentPermissionsTest() {
        MetaPermissionGroup metaPermissionGroup = getMetaPermissionGroupRandomSampleGenerator();
        DocumentPermission documentPermissionBack = getDocumentPermissionRandomSampleGenerator();

        metaPermissionGroup.addDocumentPermissions(documentPermissionBack);
        assertThat(metaPermissionGroup.getDocumentPermissions()).containsOnly(documentPermissionBack);
        assertThat(documentPermissionBack.getMetaPermissionGroup()).isEqualTo(metaPermissionGroup);

        metaPermissionGroup.removeDocumentPermissions(documentPermissionBack);
        assertThat(metaPermissionGroup.getDocumentPermissions()).doesNotContain(documentPermissionBack);
        assertThat(documentPermissionBack.getMetaPermissionGroup()).isNull();

        metaPermissionGroup.documentPermissions(new HashSet<>(Set.of(documentPermissionBack)));
        assertThat(metaPermissionGroup.getDocumentPermissions()).containsOnly(documentPermissionBack);
        assertThat(documentPermissionBack.getMetaPermissionGroup()).isEqualTo(metaPermissionGroup);

        metaPermissionGroup.setDocumentPermissions(new HashSet<>());
        assertThat(metaPermissionGroup.getDocumentPermissions()).doesNotContain(documentPermissionBack);
        assertThat(documentPermissionBack.getMetaPermissionGroup()).isNull();
    }
}
