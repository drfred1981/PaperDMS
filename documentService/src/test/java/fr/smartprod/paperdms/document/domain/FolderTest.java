package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.FolderTestSamples.*;
import static fr.smartprod.paperdms.document.domain.FolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Folder.class);
        Folder folder1 = getFolderSample1();
        Folder folder2 = new Folder();
        assertThat(folder1).isNotEqualTo(folder2);

        folder2.setId(folder1.getId());
        assertThat(folder1).isEqualTo(folder2);

        folder2 = getFolderSample2();
        assertThat(folder1).isNotEqualTo(folder2);
    }

    @Test
    void childrenTest() {
        Folder folder = getFolderRandomSampleGenerator();
        Folder folderBack = getFolderRandomSampleGenerator();

        folder.addChildren(folderBack);
        assertThat(folder.getChildren()).containsOnly(folderBack);
        assertThat(folderBack.getParent()).isEqualTo(folder);

        folder.removeChildren(folderBack);
        assertThat(folder.getChildren()).doesNotContain(folderBack);
        assertThat(folderBack.getParent()).isNull();

        folder.children(new HashSet<>(Set.of(folderBack)));
        assertThat(folder.getChildren()).containsOnly(folderBack);
        assertThat(folderBack.getParent()).isEqualTo(folder);

        folder.setChildren(new HashSet<>());
        assertThat(folder.getChildren()).doesNotContain(folderBack);
        assertThat(folderBack.getParent()).isNull();
    }

    @Test
    void parentTest() {
        Folder folder = getFolderRandomSampleGenerator();
        Folder folderBack = getFolderRandomSampleGenerator();

        folder.setParent(folderBack);
        assertThat(folder.getParent()).isEqualTo(folderBack);

        folder.parent(null);
        assertThat(folder.getParent()).isNull();
    }
}
