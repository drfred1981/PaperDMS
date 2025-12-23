package fr.smartprod.paperdms.archive.domain;

import static fr.smartprod.paperdms.archive.domain.ArchiveJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.archive.web.rest.TestUtil;
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
}
