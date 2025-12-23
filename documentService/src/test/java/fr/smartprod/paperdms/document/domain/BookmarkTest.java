package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.BookmarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookmarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bookmark.class);
        Bookmark bookmark1 = getBookmarkSample1();
        Bookmark bookmark2 = new Bookmark();
        assertThat(bookmark1).isNotEqualTo(bookmark2);

        bookmark2.setId(bookmark1.getId());
        assertThat(bookmark1).isEqualTo(bookmark2);

        bookmark2 = getBookmarkSample2();
        assertThat(bookmark1).isNotEqualTo(bookmark2);
    }
}
