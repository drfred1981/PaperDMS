package com.ged.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ged.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagPredictionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagPredictionDTO.class);
        TagPredictionDTO tagPredictionDTO1 = new TagPredictionDTO();
        tagPredictionDTO1.setId(1L);
        TagPredictionDTO tagPredictionDTO2 = new TagPredictionDTO();
        assertThat(tagPredictionDTO1).isNotEqualTo(tagPredictionDTO2);
        tagPredictionDTO2.setId(tagPredictionDTO1.getId());
        assertThat(tagPredictionDTO1).isEqualTo(tagPredictionDTO2);
        tagPredictionDTO2.setId(2L);
        assertThat(tagPredictionDTO1).isNotEqualTo(tagPredictionDTO2);
        tagPredictionDTO1.setId(null);
        assertThat(tagPredictionDTO1).isNotEqualTo(tagPredictionDTO2);
    }
}
