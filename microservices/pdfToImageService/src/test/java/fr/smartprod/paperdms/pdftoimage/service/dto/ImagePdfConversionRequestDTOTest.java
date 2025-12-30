package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagePdfConversionRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagePdfConversionRequestDTO.class);
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO1 = new ImagePdfConversionRequestDTO();
        imagePdfConversionRequestDTO1.setId(1L);
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO2 = new ImagePdfConversionRequestDTO();
        assertThat(imagePdfConversionRequestDTO1).isNotEqualTo(imagePdfConversionRequestDTO2);
        imagePdfConversionRequestDTO2.setId(imagePdfConversionRequestDTO1.getId());
        assertThat(imagePdfConversionRequestDTO1).isEqualTo(imagePdfConversionRequestDTO2);
        imagePdfConversionRequestDTO2.setId(2L);
        assertThat(imagePdfConversionRequestDTO1).isNotEqualTo(imagePdfConversionRequestDTO2);
        imagePdfConversionRequestDTO1.setId(null);
        assertThat(imagePdfConversionRequestDTO1).isNotEqualTo(imagePdfConversionRequestDTO2);
    }
}
