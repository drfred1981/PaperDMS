package fr.smartprod.paperdms.archive.domain;

import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentTestSamples.*;
import static fr.smartprod.paperdms.archive.domain.ArchiveJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.archive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchiveDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveDocument.class);
        ArchiveDocument archiveDocument1 = getArchiveDocumentSample1();
        ArchiveDocument archiveDocument2 = new ArchiveDocument();
        assertThat(archiveDocument1).isNotEqualTo(archiveDocument2);

        archiveDocument2.setId(archiveDocument1.getId());
        assertThat(archiveDocument1).isEqualTo(archiveDocument2);

        archiveDocument2 = getArchiveDocumentSample2();
        assertThat(archiveDocument1).isNotEqualTo(archiveDocument2);
    }

    @Test
    void archiveJobTest() {
        ArchiveDocument archiveDocument = getArchiveDocumentRandomSampleGenerator();
        ArchiveJob archiveJobBack = getArchiveJobRandomSampleGenerator();

        archiveDocument.setArchiveJob(archiveJobBack);
        assertThat(archiveDocument.getArchiveJob()).isEqualTo(archiveJobBack);

        archiveDocument.archiveJob(null);
        assertThat(archiveDocument.getArchiveJob()).isNull();
    }
}
