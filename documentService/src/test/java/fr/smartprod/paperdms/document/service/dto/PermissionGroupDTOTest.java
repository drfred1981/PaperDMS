package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionGroupDTO.class);
        PermissionGroupDTO permissionGroupDTO1 = new PermissionGroupDTO();
        permissionGroupDTO1.setId(1L);
        PermissionGroupDTO permissionGroupDTO2 = new PermissionGroupDTO();
        assertThat(permissionGroupDTO1).isNotEqualTo(permissionGroupDTO2);
        permissionGroupDTO2.setId(permissionGroupDTO1.getId());
        assertThat(permissionGroupDTO1).isEqualTo(permissionGroupDTO2);
        permissionGroupDTO2.setId(2L);
        assertThat(permissionGroupDTO1).isNotEqualTo(permissionGroupDTO2);
        permissionGroupDTO1.setId(null);
        assertThat(permissionGroupDTO1).isNotEqualTo(permissionGroupDTO2);
    }
}
