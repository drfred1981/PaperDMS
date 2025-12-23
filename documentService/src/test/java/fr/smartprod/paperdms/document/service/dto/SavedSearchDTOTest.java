package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SavedSearchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SavedSearchDTO.class);
        SavedSearchDTO savedSearchDTO1 = new SavedSearchDTO();
        savedSearchDTO1.setId(1L);
        SavedSearchDTO savedSearchDTO2 = new SavedSearchDTO();
        assertThat(savedSearchDTO1).isNotEqualTo(savedSearchDTO2);
        savedSearchDTO2.setId(savedSearchDTO1.getId());
        assertThat(savedSearchDTO1).isEqualTo(savedSearchDTO2);
        savedSearchDTO2.setId(2L);
        assertThat(savedSearchDTO1).isNotEqualTo(savedSearchDTO2);
        savedSearchDTO1.setId(null);
        assertThat(savedSearchDTO1).isNotEqualTo(savedSearchDTO2);
    }
}
