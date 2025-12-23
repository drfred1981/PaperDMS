package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.ManualChapterTestSamples.*;
import static fr.smartprod.paperdms.business.domain.ManualChapterTestSamples.*;
import static fr.smartprod.paperdms.business.domain.ManualTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ManualChapterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualChapter.class);
        ManualChapter manualChapter1 = getManualChapterSample1();
        ManualChapter manualChapter2 = new ManualChapter();
        assertThat(manualChapter1).isNotEqualTo(manualChapter2);

        manualChapter2.setId(manualChapter1.getId());
        assertThat(manualChapter1).isEqualTo(manualChapter2);

        manualChapter2 = getManualChapterSample2();
        assertThat(manualChapter1).isNotEqualTo(manualChapter2);
    }

    @Test
    void subchaptersTest() {
        ManualChapter manualChapter = getManualChapterRandomSampleGenerator();
        ManualChapter manualChapterBack = getManualChapterRandomSampleGenerator();

        manualChapter.addSubchapters(manualChapterBack);
        assertThat(manualChapter.getSubchapters()).containsOnly(manualChapterBack);
        assertThat(manualChapterBack.getParentChapter()).isEqualTo(manualChapter);

        manualChapter.removeSubchapters(manualChapterBack);
        assertThat(manualChapter.getSubchapters()).doesNotContain(manualChapterBack);
        assertThat(manualChapterBack.getParentChapter()).isNull();

        manualChapter.subchapters(new HashSet<>(Set.of(manualChapterBack)));
        assertThat(manualChapter.getSubchapters()).containsOnly(manualChapterBack);
        assertThat(manualChapterBack.getParentChapter()).isEqualTo(manualChapter);

        manualChapter.setSubchapters(new HashSet<>());
        assertThat(manualChapter.getSubchapters()).doesNotContain(manualChapterBack);
        assertThat(manualChapterBack.getParentChapter()).isNull();
    }

    @Test
    void manualTest() {
        ManualChapter manualChapter = getManualChapterRandomSampleGenerator();
        Manual manualBack = getManualRandomSampleGenerator();

        manualChapter.setManual(manualBack);
        assertThat(manualChapter.getManual()).isEqualTo(manualBack);

        manualChapter.manual(null);
        assertThat(manualChapter.getManual()).isNull();
    }

    @Test
    void parentChapterTest() {
        ManualChapter manualChapter = getManualChapterRandomSampleGenerator();
        ManualChapter manualChapterBack = getManualChapterRandomSampleGenerator();

        manualChapter.setParentChapter(manualChapterBack);
        assertThat(manualChapter.getParentChapter()).isEqualTo(manualChapterBack);

        manualChapter.parentChapter(null);
        assertThat(manualChapter.getParentChapter()).isNull();
    }
}
