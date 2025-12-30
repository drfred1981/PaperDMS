package fr.smartprod.paperdms.archive.domain;

import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentTestSamples.*;
import static fr.smartprod.paperdms.archive.domain.ArchiveJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.archive.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ArchiveJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveJob.class);
        ArchiveJob archiveJob1 = getArchiveJobSample1();
        ArchiveJob archiveJob2 = new ArchiveJob();
        assertThat(archiveJob1).isNotEqualTo(archiveJob2);

        archiveJob2.setId(archiveJob1.getId());
        assertThat(archiveJob1).isEqualTo(archiveJob2);

        archiveJob2 = getArchiveJobSample2();
        assertThat(archiveJob1).isNotEqualTo(archiveJob2);
    }

    @Test
    void archivesDocumentsTest() {
        ArchiveJob archiveJob = getArchiveJobRandomSampleGenerator();
        ArchiveDocument archiveDocumentBack = getArchiveDocumentRandomSampleGenerator();

        archiveJob.addArchivesDocuments(archiveDocumentBack);
        assertThat(archiveJob.getArchivesDocuments()).containsOnly(archiveDocumentBack);
        assertThat(archiveDocumentBack.getArchiveJob()).isEqualTo(archiveJob);

        archiveJob.removeArchivesDocuments(archiveDocumentBack);
        assertThat(archiveJob.getArchivesDocuments()).doesNotContain(archiveDocumentBack);
        assertThat(archiveDocumentBack.getArchiveJob()).isNull();

        archiveJob.archivesDocuments(new HashSet<>(Set.of(archiveDocumentBack)));
        assertThat(archiveJob.getArchivesDocuments()).containsOnly(archiveDocumentBack);
        assertThat(archiveDocumentBack.getArchiveJob()).isEqualTo(archiveJob);

        archiveJob.setArchivesDocuments(new HashSet<>());
        assertThat(archiveJob.getArchivesDocuments()).doesNotContain(archiveDocumentBack);
        assertThat(archiveDocumentBack.getArchiveJob()).isNull();
    }
}
