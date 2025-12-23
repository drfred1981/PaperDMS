package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.PermissionGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionGroup.class);
        PermissionGroup permissionGroup1 = getPermissionGroupSample1();
        PermissionGroup permissionGroup2 = new PermissionGroup();
        assertThat(permissionGroup1).isNotEqualTo(permissionGroup2);

        permissionGroup2.setId(permissionGroup1.getId());
        assertThat(permissionGroup1).isEqualTo(permissionGroup2);

        permissionGroup2 = getPermissionGroupSample2();
        assertThat(permissionGroup1).isNotEqualTo(permissionGroup2);
    }
}
