package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.SavedSearchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SavedSearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SavedSearch.class);
        SavedSearch savedSearch1 = getSavedSearchSample1();
        SavedSearch savedSearch2 = new SavedSearch();
        assertThat(savedSearch1).isNotEqualTo(savedSearch2);

        savedSearch2.setId(savedSearch1.getId());
        assertThat(savedSearch1).isEqualTo(savedSearch2);

        savedSearch2 = getSavedSearchSample2();
        assertThat(savedSearch1).isNotEqualTo(savedSearch2);
    }
}
