package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.ExtractedFieldTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtractedFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtractedField.class);
        ExtractedField extractedField1 = getExtractedFieldSample1();
        ExtractedField extractedField2 = new ExtractedField();
        assertThat(extractedField1).isNotEqualTo(extractedField2);

        extractedField2.setId(extractedField1.getId());
        assertThat(extractedField1).isEqualTo(extractedField2);

        extractedField2 = getExtractedFieldSample2();
        assertThat(extractedField1).isNotEqualTo(extractedField2);
    }
}
