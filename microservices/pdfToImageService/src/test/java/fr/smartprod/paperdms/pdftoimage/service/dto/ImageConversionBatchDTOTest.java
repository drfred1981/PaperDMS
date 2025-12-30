package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionBatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionBatchDTO.class);
        ImageConversionBatchDTO imageConversionBatchDTO1 = new ImageConversionBatchDTO();
        imageConversionBatchDTO1.setId(1L);
        ImageConversionBatchDTO imageConversionBatchDTO2 = new ImageConversionBatchDTO();
        assertThat(imageConversionBatchDTO1).isNotEqualTo(imageConversionBatchDTO2);
        imageConversionBatchDTO2.setId(imageConversionBatchDTO1.getId());
        assertThat(imageConversionBatchDTO1).isEqualTo(imageConversionBatchDTO2);
        imageConversionBatchDTO2.setId(2L);
        assertThat(imageConversionBatchDTO1).isNotEqualTo(imageConversionBatchDTO2);
        imageConversionBatchDTO1.setId(null);
        assertThat(imageConversionBatchDTO1).isNotEqualTo(imageConversionBatchDTO2);
    }
}
