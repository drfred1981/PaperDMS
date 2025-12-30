package fr.smartprod.paperdms.export.domain;

import static fr.smartprod.paperdms.export.domain.ExportJobTestSamples.*;
import static fr.smartprod.paperdms.export.domain.ExportResultTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExportResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportResult.class);
        ExportResult exportResult1 = getExportResultSample1();
        ExportResult exportResult2 = new ExportResult();
        assertThat(exportResult1).isNotEqualTo(exportResult2);

        exportResult2.setId(exportResult1.getId());
        assertThat(exportResult1).isEqualTo(exportResult2);

        exportResult2 = getExportResultSample2();
        assertThat(exportResult1).isNotEqualTo(exportResult2);
    }

    @Test
    void exportJobTest() {
        ExportResult exportResult = getExportResultRandomSampleGenerator();
        ExportJob exportJobBack = getExportJobRandomSampleGenerator();

        exportResult.setExportJob(exportJobBack);
        assertThat(exportResult.getExportJob()).isEqualTo(exportJobBack);

        exportResult.exportJob(null);
        assertThat(exportResult.getExportJob()).isNull();
    }
}
