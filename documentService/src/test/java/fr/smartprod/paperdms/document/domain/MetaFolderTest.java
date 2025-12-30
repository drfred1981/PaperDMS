package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaFolderTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaFolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MetaFolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaFolder.class);
        MetaFolder metaFolder1 = getMetaFolderSample1();
        MetaFolder metaFolder2 = new MetaFolder();
        assertThat(metaFolder1).isNotEqualTo(metaFolder2);

        metaFolder2.setId(metaFolder1.getId());
        assertThat(metaFolder1).isEqualTo(metaFolder2);

        metaFolder2 = getMetaFolderSample2();
        assertThat(metaFolder1).isNotEqualTo(metaFolder2);
    }

    @Test
    void childrenTest() {
        MetaFolder metaFolder = getMetaFolderRandomSampleGenerator();
        MetaFolder metaFolderBack = getMetaFolderRandomSampleGenerator();

        metaFolder.addChildren(metaFolderBack);
        assertThat(metaFolder.getChildren()).containsOnly(metaFolderBack);
        assertThat(metaFolderBack.getParent()).isEqualTo(metaFolder);

        metaFolder.removeChildren(metaFolderBack);
        assertThat(metaFolder.getChildren()).doesNotContain(metaFolderBack);
        assertThat(metaFolderBack.getParent()).isNull();

        metaFolder.children(new HashSet<>(Set.of(metaFolderBack)));
        assertThat(metaFolder.getChildren()).containsOnly(metaFolderBack);
        assertThat(metaFolderBack.getParent()).isEqualTo(metaFolder);

        metaFolder.setChildren(new HashSet<>());
        assertThat(metaFolder.getChildren()).doesNotContain(metaFolderBack);
        assertThat(metaFolderBack.getParent()).isNull();
    }

    @Test
    void documentsTest() {
        MetaFolder metaFolder = getMetaFolderRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        metaFolder.addDocuments(documentBack);
        assertThat(metaFolder.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getFolder()).isEqualTo(metaFolder);

        metaFolder.removeDocuments(documentBack);
        assertThat(metaFolder.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getFolder()).isNull();

        metaFolder.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(metaFolder.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getFolder()).isEqualTo(metaFolder);

        metaFolder.setDocuments(new HashSet<>());
        assertThat(metaFolder.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getFolder()).isNull();
    }

    @Test
    void parentTest() {
        MetaFolder metaFolder = getMetaFolderRandomSampleGenerator();
        MetaFolder metaFolderBack = getMetaFolderRandomSampleGenerator();

        metaFolder.setParent(metaFolderBack);
        assertThat(metaFolder.getParent()).isEqualTo(metaFolderBack);

        metaFolder.parent(null);
        assertThat(metaFolder.getParent()).isNull();
    }
}
