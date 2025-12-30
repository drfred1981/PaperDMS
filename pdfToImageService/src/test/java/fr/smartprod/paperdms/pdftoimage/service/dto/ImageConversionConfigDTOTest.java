package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionConfigDTO.class);
        ImageConversionConfigDTO imageConversionConfigDTO1 = new ImageConversionConfigDTO();
        imageConversionConfigDTO1.setId(1L);
        ImageConversionConfigDTO imageConversionConfigDTO2 = new ImageConversionConfigDTO();
        assertThat(imageConversionConfigDTO1).isNotEqualTo(imageConversionConfigDTO2);
        imageConversionConfigDTO2.setId(imageConversionConfigDTO1.getId());
        assertThat(imageConversionConfigDTO1).isEqualTo(imageConversionConfigDTO2);
        imageConversionConfigDTO2.setId(2L);
        assertThat(imageConversionConfigDTO1).isNotEqualTo(imageConversionConfigDTO2);
        imageConversionConfigDTO1.setId(null);
        assertThat(imageConversionConfigDTO1).isNotEqualTo(imageConversionConfigDTO2);
    }
}
