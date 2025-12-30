package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.MetaSavedSearchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaSavedSearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaSavedSearch.class);
        MetaSavedSearch metaSavedSearch1 = getMetaSavedSearchSample1();
        MetaSavedSearch metaSavedSearch2 = new MetaSavedSearch();
        assertThat(metaSavedSearch1).isNotEqualTo(metaSavedSearch2);

        metaSavedSearch2.setId(metaSavedSearch1.getId());
        assertThat(metaSavedSearch1).isEqualTo(metaSavedSearch2);

        metaSavedSearch2 = getMetaSavedSearchSample2();
        assertThat(metaSavedSearch1).isNotEqualTo(metaSavedSearch2);
    }
}
