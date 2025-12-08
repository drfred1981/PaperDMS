package com.ged.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrespondentExtractionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrespondentExtractionDTO.class);
        CorrespondentExtractionDTO correspondentExtractionDTO1 = new CorrespondentExtractionDTO();
        correspondentExtractionDTO1.setId(1L);
        CorrespondentExtractionDTO correspondentExtractionDTO2 = new CorrespondentExtractionDTO();
        assertThat(correspondentExtractionDTO1).isNotEqualTo(correspondentExtractionDTO2);
        correspondentExtractionDTO2.setId(correspondentExtractionDTO1.getId());
        assertThat(correspondentExtractionDTO1).isEqualTo(correspondentExtractionDTO2);
        correspondentExtractionDTO2.setId(2L);
        assertThat(correspondentExtractionDTO1).isNotEqualTo(correspondentExtractionDTO2);
        correspondentExtractionDTO1.setId(null);
        assertThat(correspondentExtractionDTO1).isNotEqualTo(correspondentExtractionDTO2);
    }
}
