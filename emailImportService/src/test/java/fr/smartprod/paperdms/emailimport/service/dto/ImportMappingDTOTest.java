package fr.smartprod.paperdms.emailimport.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.emailimport.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImportMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImportMappingDTO.class);
        ImportMappingDTO importMappingDTO1 = new ImportMappingDTO();
        importMappingDTO1.setId(1L);
        ImportMappingDTO importMappingDTO2 = new ImportMappingDTO();
        assertThat(importMappingDTO1).isNotEqualTo(importMappingDTO2);
        importMappingDTO2.setId(importMappingDTO1.getId());
        assertThat(importMappingDTO1).isEqualTo(importMappingDTO2);
        importMappingDTO2.setId(2L);
        assertThat(importMappingDTO1).isNotEqualTo(importMappingDTO2);
        importMappingDTO1.setId(null);
        assertThat(importMappingDTO1).isNotEqualTo(importMappingDTO2);
    }
}
