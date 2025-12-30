package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionHistoryDTO.class);
        ImageConversionHistoryDTO imageConversionHistoryDTO1 = new ImageConversionHistoryDTO();
        imageConversionHistoryDTO1.setId(1L);
        ImageConversionHistoryDTO imageConversionHistoryDTO2 = new ImageConversionHistoryDTO();
        assertThat(imageConversionHistoryDTO1).isNotEqualTo(imageConversionHistoryDTO2);
        imageConversionHistoryDTO2.setId(imageConversionHistoryDTO1.getId());
        assertThat(imageConversionHistoryDTO1).isEqualTo(imageConversionHistoryDTO2);
        imageConversionHistoryDTO2.setId(2L);
        assertThat(imageConversionHistoryDTO1).isNotEqualTo(imageConversionHistoryDTO2);
        imageConversionHistoryDTO1.setId(null);
        assertThat(imageConversionHistoryDTO1).isNotEqualTo(imageConversionHistoryDTO2);
    }
}
