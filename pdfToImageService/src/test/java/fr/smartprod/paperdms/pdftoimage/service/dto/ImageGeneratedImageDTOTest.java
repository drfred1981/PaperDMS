package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageGeneratedImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageGeneratedImageDTO.class);
        ImageGeneratedImageDTO imageGeneratedImageDTO1 = new ImageGeneratedImageDTO();
        imageGeneratedImageDTO1.setId(1L);
        ImageGeneratedImageDTO imageGeneratedImageDTO2 = new ImageGeneratedImageDTO();
        assertThat(imageGeneratedImageDTO1).isNotEqualTo(imageGeneratedImageDTO2);
        imageGeneratedImageDTO2.setId(imageGeneratedImageDTO1.getId());
        assertThat(imageGeneratedImageDTO1).isEqualTo(imageGeneratedImageDTO2);
        imageGeneratedImageDTO2.setId(2L);
        assertThat(imageGeneratedImageDTO1).isNotEqualTo(imageGeneratedImageDTO2);
        imageGeneratedImageDTO1.setId(null);
        assertThat(imageGeneratedImageDTO1).isNotEqualTo(imageGeneratedImageDTO2);
    }
}
