package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.MetaBookmarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaBookmarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaBookmark.class);
        MetaBookmark metaBookmark1 = getMetaBookmarkSample1();
        MetaBookmark metaBookmark2 = new MetaBookmark();
        assertThat(metaBookmark1).isNotEqualTo(metaBookmark2);

        metaBookmark2.setId(metaBookmark1.getId());
        assertThat(metaBookmark1).isEqualTo(metaBookmark2);

        metaBookmark2 = getMetaBookmarkSample2();
        assertThat(metaBookmark1).isNotEqualTo(metaBookmark2);
    }
}
