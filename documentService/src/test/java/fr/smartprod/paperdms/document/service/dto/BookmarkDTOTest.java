package fr.smartprod.paperdms.document.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookmarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookmarkDTO.class);
        BookmarkDTO bookmarkDTO1 = new BookmarkDTO();
        bookmarkDTO1.setId(1L);
        BookmarkDTO bookmarkDTO2 = new BookmarkDTO();
        assertThat(bookmarkDTO1).isNotEqualTo(bookmarkDTO2);
        bookmarkDTO2.setId(bookmarkDTO1.getId());
        assertThat(bookmarkDTO1).isEqualTo(bookmarkDTO2);
        bookmarkDTO2.setId(2L);
        assertThat(bookmarkDTO1).isNotEqualTo(bookmarkDTO2);
        bookmarkDTO1.setId(null);
        assertThat(bookmarkDTO1).isNotEqualTo(bookmarkDTO2);
    }
}
