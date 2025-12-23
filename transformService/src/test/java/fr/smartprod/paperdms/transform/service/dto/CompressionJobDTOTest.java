package fr.smartprod.paperdms.transform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompressionJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompressionJobDTO.class);
        CompressionJobDTO compressionJobDTO1 = new CompressionJobDTO();
        compressionJobDTO1.setId(1L);
        CompressionJobDTO compressionJobDTO2 = new CompressionJobDTO();
        assertThat(compressionJobDTO1).isNotEqualTo(compressionJobDTO2);
        compressionJobDTO2.setId(compressionJobDTO1.getId());
        assertThat(compressionJobDTO1).isEqualTo(compressionJobDTO2);
        compressionJobDTO2.setId(2L);
        assertThat(compressionJobDTO1).isNotEqualTo(compressionJobDTO2);
        compressionJobDTO1.setId(null);
        assertThat(compressionJobDTO1).isNotEqualTo(compressionJobDTO2);
    }
}
