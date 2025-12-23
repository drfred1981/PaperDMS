package fr.smartprod.paperdms.business.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualChapterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualChapterDTO.class);
        ManualChapterDTO manualChapterDTO1 = new ManualChapterDTO();
        manualChapterDTO1.setId(1L);
        ManualChapterDTO manualChapterDTO2 = new ManualChapterDTO();
        assertThat(manualChapterDTO1).isNotEqualTo(manualChapterDTO2);
        manualChapterDTO2.setId(manualChapterDTO1.getId());
        assertThat(manualChapterDTO1).isEqualTo(manualChapterDTO2);
        manualChapterDTO2.setId(2L);
        assertThat(manualChapterDTO1).isNotEqualTo(manualChapterDTO2);
        manualChapterDTO1.setId(null);
        assertThat(manualChapterDTO1).isNotEqualTo(manualChapterDTO2);
    }
}
