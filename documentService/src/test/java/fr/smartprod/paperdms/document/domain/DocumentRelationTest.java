package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentRelationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentRelation.class);
        DocumentRelation documentRelation1 = getDocumentRelationSample1();
        DocumentRelation documentRelation2 = new DocumentRelation();
        assertThat(documentRelation1).isNotEqualTo(documentRelation2);

        documentRelation2.setId(documentRelation1.getId());
        assertThat(documentRelation1).isEqualTo(documentRelation2);

        documentRelation2 = getDocumentRelationSample2();
        assertThat(documentRelation1).isNotEqualTo(documentRelation2);
    }
}
