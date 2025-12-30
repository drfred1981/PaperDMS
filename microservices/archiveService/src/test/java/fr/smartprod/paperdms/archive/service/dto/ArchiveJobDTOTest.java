package fr.smartprod.paperdms.archive.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.archive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchiveJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveJobDTO.class);
        ArchiveJobDTO archiveJobDTO1 = new ArchiveJobDTO();
        archiveJobDTO1.setId(1L);
        ArchiveJobDTO archiveJobDTO2 = new ArchiveJobDTO();
        assertThat(archiveJobDTO1).isNotEqualTo(archiveJobDTO2);
        archiveJobDTO2.setId(archiveJobDTO1.getId());
        assertThat(archiveJobDTO1).isEqualTo(archiveJobDTO2);
        archiveJobDTO2.setId(2L);
        assertThat(archiveJobDTO1).isNotEqualTo(archiveJobDTO2);
        archiveJobDTO1.setId(null);
        assertThat(archiveJobDTO1).isNotEqualTo(archiveJobDTO2);
    }
}
