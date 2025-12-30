package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaSavedSearchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaSavedSearchDTO.class);
        MetaSavedSearchDTO metaSavedSearchDTO1 = new MetaSavedSearchDTO();
        metaSavedSearchDTO1.setId(1L);
        MetaSavedSearchDTO metaSavedSearchDTO2 = new MetaSavedSearchDTO();
        assertThat(metaSavedSearchDTO1).isNotEqualTo(metaSavedSearchDTO2);
        metaSavedSearchDTO2.setId(metaSavedSearchDTO1.getId());
        assertThat(metaSavedSearchDTO1).isEqualTo(metaSavedSearchDTO2);
        metaSavedSearchDTO2.setId(2L);
        assertThat(metaSavedSearchDTO1).isNotEqualTo(metaSavedSearchDTO2);
        metaSavedSearchDTO1.setId(null);
        assertThat(metaSavedSearchDTO1).isNotEqualTo(metaSavedSearchDTO2);
    }
}
