package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentStatisticsDTO.class);
        DocumentStatisticsDTO documentStatisticsDTO1 = new DocumentStatisticsDTO();
        documentStatisticsDTO1.setId(1L);
        DocumentStatisticsDTO documentStatisticsDTO2 = new DocumentStatisticsDTO();
        assertThat(documentStatisticsDTO1).isNotEqualTo(documentStatisticsDTO2);
        documentStatisticsDTO2.setId(documentStatisticsDTO1.getId());
        assertThat(documentStatisticsDTO1).isEqualTo(documentStatisticsDTO2);
        documentStatisticsDTO2.setId(2L);
        assertThat(documentStatisticsDTO1).isNotEqualTo(documentStatisticsDTO2);
        documentStatisticsDTO1.setId(null);
        assertThat(documentStatisticsDTO1).isNotEqualTo(documentStatisticsDTO2);
    }
}
