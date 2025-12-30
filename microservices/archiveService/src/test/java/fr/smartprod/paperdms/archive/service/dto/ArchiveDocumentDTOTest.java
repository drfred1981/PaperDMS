package fr.smartprod.paperdms.archive.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.archive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchiveDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveDocumentDTO.class);
        ArchiveDocumentDTO archiveDocumentDTO1 = new ArchiveDocumentDTO();
        archiveDocumentDTO1.setId(1L);
        ArchiveDocumentDTO archiveDocumentDTO2 = new ArchiveDocumentDTO();
        assertThat(archiveDocumentDTO1).isNotEqualTo(archiveDocumentDTO2);
        archiveDocumentDTO2.setId(archiveDocumentDTO1.getId());
        assertThat(archiveDocumentDTO1).isEqualTo(archiveDocumentDTO2);
        archiveDocumentDTO2.setId(2L);
        assertThat(archiveDocumentDTO1).isNotEqualTo(archiveDocumentDTO2);
        archiveDocumentDTO1.setId(null);
        assertThat(archiveDocumentDTO1).isNotEqualTo(archiveDocumentDTO2);
    }
}
