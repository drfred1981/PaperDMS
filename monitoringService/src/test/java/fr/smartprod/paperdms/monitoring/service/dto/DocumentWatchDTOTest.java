package fr.smartprod.paperdms.monitoring.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentWatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentWatchDTO.class);
        DocumentWatchDTO documentWatchDTO1 = new DocumentWatchDTO();
        documentWatchDTO1.setId(1L);
        DocumentWatchDTO documentWatchDTO2 = new DocumentWatchDTO();
        assertThat(documentWatchDTO1).isNotEqualTo(documentWatchDTO2);
        documentWatchDTO2.setId(documentWatchDTO1.getId());
        assertThat(documentWatchDTO1).isEqualTo(documentWatchDTO2);
        documentWatchDTO2.setId(2L);
        assertThat(documentWatchDTO1).isNotEqualTo(documentWatchDTO2);
        documentWatchDTO1.setId(null);
        assertThat(documentWatchDTO1).isNotEqualTo(documentWatchDTO2);
    }
}
