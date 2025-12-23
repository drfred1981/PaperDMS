package fr.smartprod.paperdms.ai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrespondentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrespondentDTO.class);
        CorrespondentDTO correspondentDTO1 = new CorrespondentDTO();
        correspondentDTO1.setId(1L);
        CorrespondentDTO correspondentDTO2 = new CorrespondentDTO();
        assertThat(correspondentDTO1).isNotEqualTo(correspondentDTO2);
        correspondentDTO2.setId(correspondentDTO1.getId());
        assertThat(correspondentDTO1).isEqualTo(correspondentDTO2);
        correspondentDTO2.setId(2L);
        assertThat(correspondentDTO1).isNotEqualTo(correspondentDTO2);
        correspondentDTO1.setId(null);
        assertThat(correspondentDTO1).isNotEqualTo(correspondentDTO2);
    }
}
