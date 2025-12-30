package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaPermissionGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaPermissionGroupDTO.class);
        MetaPermissionGroupDTO metaPermissionGroupDTO1 = new MetaPermissionGroupDTO();
        metaPermissionGroupDTO1.setId(1L);
        MetaPermissionGroupDTO metaPermissionGroupDTO2 = new MetaPermissionGroupDTO();
        assertThat(metaPermissionGroupDTO1).isNotEqualTo(metaPermissionGroupDTO2);
        metaPermissionGroupDTO2.setId(metaPermissionGroupDTO1.getId());
        assertThat(metaPermissionGroupDTO1).isEqualTo(metaPermissionGroupDTO2);
        metaPermissionGroupDTO2.setId(2L);
        assertThat(metaPermissionGroupDTO1).isNotEqualTo(metaPermissionGroupDTO2);
        metaPermissionGroupDTO1.setId(null);
        assertThat(metaPermissionGroupDTO1).isNotEqualTo(metaPermissionGroupDTO2);
    }
}
