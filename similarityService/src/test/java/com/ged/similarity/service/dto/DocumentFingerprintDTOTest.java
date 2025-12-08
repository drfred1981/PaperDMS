package com.ged.similarity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentFingerprintDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFingerprintDTO.class);
        DocumentFingerprintDTO documentFingerprintDTO1 = new DocumentFingerprintDTO();
        documentFingerprintDTO1.setId(1L);
        DocumentFingerprintDTO documentFingerprintDTO2 = new DocumentFingerprintDTO();
        assertThat(documentFingerprintDTO1).isNotEqualTo(documentFingerprintDTO2);
        documentFingerprintDTO2.setId(documentFingerprintDTO1.getId());
        assertThat(documentFingerprintDTO1).isEqualTo(documentFingerprintDTO2);
        documentFingerprintDTO2.setId(2L);
        assertThat(documentFingerprintDTO1).isNotEqualTo(documentFingerprintDTO2);
        documentFingerprintDTO1.setId(null);
        assertThat(documentFingerprintDTO1).isNotEqualTo(documentFingerprintDTO2);
    }
}
