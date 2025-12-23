package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentMetadataDTO.class);
        DocumentMetadataDTO documentMetadataDTO1 = new DocumentMetadataDTO();
        documentMetadataDTO1.setId(1L);
        DocumentMetadataDTO documentMetadataDTO2 = new DocumentMetadataDTO();
        assertThat(documentMetadataDTO1).isNotEqualTo(documentMetadataDTO2);
        documentMetadataDTO2.setId(documentMetadataDTO1.getId());
        assertThat(documentMetadataDTO1).isEqualTo(documentMetadataDTO2);
        documentMetadataDTO2.setId(2L);
        assertThat(documentMetadataDTO1).isNotEqualTo(documentMetadataDTO2);
        documentMetadataDTO1.setId(null);
        assertThat(documentMetadataDTO1).isNotEqualTo(documentMetadataDTO2);
    }
}
