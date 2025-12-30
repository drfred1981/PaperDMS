package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentAuditTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentCommentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentExtractedFieldTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentMetadataTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentPermissionTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentServiceStatusTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentStatisticsTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTagTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTypeTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentVersionTestSamples.*;
import static fr.smartprod.paperdms.document.domain.MetaFolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void documentVersionsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentVersion documentVersionBack = getDocumentVersionRandomSampleGenerator();

        document.addDocumentVersions(documentVersionBack);
        assertThat(document.getDocumentVersions()).containsOnly(documentVersionBack);
        assertThat(documentVersionBack.getDocument()).isEqualTo(document);

        document.removeDocumentVersions(documentVersionBack);
        assertThat(document.getDocumentVersions()).doesNotContain(documentVersionBack);
        assertThat(documentVersionBack.getDocument()).isNull();

        document.documentVersions(new HashSet<>(Set.of(documentVersionBack)));
        assertThat(document.getDocumentVersions()).containsOnly(documentVersionBack);
        assertThat(documentVersionBack.getDocument()).isEqualTo(document);

        document.setDocumentVersions(new HashSet<>());
        assertThat(document.getDocumentVersions()).doesNotContain(documentVersionBack);
        assertThat(documentVersionBack.getDocument()).isNull();
    }

    @Test
    void documentTagsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentTag documentTagBack = getDocumentTagRandomSampleGenerator();

        document.addDocumentTags(documentTagBack);
        assertThat(document.getDocumentTags()).containsOnly(documentTagBack);
        assertThat(documentTagBack.getDocument()).isEqualTo(document);

        document.removeDocumentTags(documentTagBack);
        assertThat(document.getDocumentTags()).doesNotContain(documentTagBack);
        assertThat(documentTagBack.getDocument()).isNull();

        document.documentTags(new HashSet<>(Set.of(documentTagBack)));
        assertThat(document.getDocumentTags()).containsOnly(documentTagBack);
        assertThat(documentTagBack.getDocument()).isEqualTo(document);

        document.setDocumentTags(new HashSet<>());
        assertThat(document.getDocumentTags()).doesNotContain(documentTagBack);
        assertThat(documentTagBack.getDocument()).isNull();
    }

    @Test
    void statusesTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentServiceStatus documentServiceStatusBack = getDocumentServiceStatusRandomSampleGenerator();

        document.addStatuses(documentServiceStatusBack);
        assertThat(document.getStatuses()).containsOnly(documentServiceStatusBack);
        assertThat(documentServiceStatusBack.getDocument()).isEqualTo(document);

        document.removeStatuses(documentServiceStatusBack);
        assertThat(document.getStatuses()).doesNotContain(documentServiceStatusBack);
        assertThat(documentServiceStatusBack.getDocument()).isNull();

        document.statuses(new HashSet<>(Set.of(documentServiceStatusBack)));
        assertThat(document.getStatuses()).containsOnly(documentServiceStatusBack);
        assertThat(documentServiceStatusBack.getDocument()).isEqualTo(document);

        document.setStatuses(new HashSet<>());
        assertThat(document.getStatuses()).doesNotContain(documentServiceStatusBack);
        assertThat(documentServiceStatusBack.getDocument()).isNull();
    }

    @Test
    void documentExtractedFieldsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentExtractedField documentExtractedFieldBack = getDocumentExtractedFieldRandomSampleGenerator();

        document.addDocumentExtractedFields(documentExtractedFieldBack);
        assertThat(document.getDocumentExtractedFields()).containsOnly(documentExtractedFieldBack);
        assertThat(documentExtractedFieldBack.getDocument()).isEqualTo(document);

        document.removeDocumentExtractedFields(documentExtractedFieldBack);
        assertThat(document.getDocumentExtractedFields()).doesNotContain(documentExtractedFieldBack);
        assertThat(documentExtractedFieldBack.getDocument()).isNull();

        document.documentExtractedFields(new HashSet<>(Set.of(documentExtractedFieldBack)));
        assertThat(document.getDocumentExtractedFields()).containsOnly(documentExtractedFieldBack);
        assertThat(documentExtractedFieldBack.getDocument()).isEqualTo(document);

        document.setDocumentExtractedFields(new HashSet<>());
        assertThat(document.getDocumentExtractedFields()).doesNotContain(documentExtractedFieldBack);
        assertThat(documentExtractedFieldBack.getDocument()).isNull();
    }

    @Test
    void permissionsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentPermission documentPermissionBack = getDocumentPermissionRandomSampleGenerator();

        document.addPermissions(documentPermissionBack);
        assertThat(document.getPermissions()).containsOnly(documentPermissionBack);
        assertThat(documentPermissionBack.getDocument()).isEqualTo(document);

        document.removePermissions(documentPermissionBack);
        assertThat(document.getPermissions()).doesNotContain(documentPermissionBack);
        assertThat(documentPermissionBack.getDocument()).isNull();

        document.permissions(new HashSet<>(Set.of(documentPermissionBack)));
        assertThat(document.getPermissions()).containsOnly(documentPermissionBack);
        assertThat(documentPermissionBack.getDocument()).isEqualTo(document);

        document.setPermissions(new HashSet<>());
        assertThat(document.getPermissions()).doesNotContain(documentPermissionBack);
        assertThat(documentPermissionBack.getDocument()).isNull();
    }

    @Test
    void auditsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentAudit documentAuditBack = getDocumentAuditRandomSampleGenerator();

        document.addAudits(documentAuditBack);
        assertThat(document.getAudits()).containsOnly(documentAuditBack);
        assertThat(documentAuditBack.getDocument()).isEqualTo(document);

        document.removeAudits(documentAuditBack);
        assertThat(document.getAudits()).doesNotContain(documentAuditBack);
        assertThat(documentAuditBack.getDocument()).isNull();

        document.audits(new HashSet<>(Set.of(documentAuditBack)));
        assertThat(document.getAudits()).containsOnly(documentAuditBack);
        assertThat(documentAuditBack.getDocument()).isEqualTo(document);

        document.setAudits(new HashSet<>());
        assertThat(document.getAudits()).doesNotContain(documentAuditBack);
        assertThat(documentAuditBack.getDocument()).isNull();
    }

    @Test
    void commentsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentComment documentCommentBack = getDocumentCommentRandomSampleGenerator();

        document.addComments(documentCommentBack);
        assertThat(document.getComments()).containsOnly(documentCommentBack);
        assertThat(documentCommentBack.getDocument()).isEqualTo(document);

        document.removeComments(documentCommentBack);
        assertThat(document.getComments()).doesNotContain(documentCommentBack);
        assertThat(documentCommentBack.getDocument()).isNull();

        document.comments(new HashSet<>(Set.of(documentCommentBack)));
        assertThat(document.getComments()).containsOnly(documentCommentBack);
        assertThat(documentCommentBack.getDocument()).isEqualTo(document);

        document.setComments(new HashSet<>());
        assertThat(document.getComments()).doesNotContain(documentCommentBack);
        assertThat(documentCommentBack.getDocument()).isNull();
    }

    @Test
    void metadatasTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentMetadata documentMetadataBack = getDocumentMetadataRandomSampleGenerator();

        document.addMetadatas(documentMetadataBack);
        assertThat(document.getMetadatas()).containsOnly(documentMetadataBack);
        assertThat(documentMetadataBack.getDocument()).isEqualTo(document);

        document.removeMetadatas(documentMetadataBack);
        assertThat(document.getMetadatas()).doesNotContain(documentMetadataBack);
        assertThat(documentMetadataBack.getDocument()).isNull();

        document.metadatas(new HashSet<>(Set.of(documentMetadataBack)));
        assertThat(document.getMetadatas()).containsOnly(documentMetadataBack);
        assertThat(documentMetadataBack.getDocument()).isEqualTo(document);

        document.setMetadatas(new HashSet<>());
        assertThat(document.getMetadatas()).doesNotContain(documentMetadataBack);
        assertThat(documentMetadataBack.getDocument()).isNull();
    }

    @Test
    void statisticsTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentStatistics documentStatisticsBack = getDocumentStatisticsRandomSampleGenerator();

        document.addStatistics(documentStatisticsBack);
        assertThat(document.getStatistics()).containsOnly(documentStatisticsBack);
        assertThat(documentStatisticsBack.getDocument()).isEqualTo(document);

        document.removeStatistics(documentStatisticsBack);
        assertThat(document.getStatistics()).doesNotContain(documentStatisticsBack);
        assertThat(documentStatisticsBack.getDocument()).isNull();

        document.statistics(new HashSet<>(Set.of(documentStatisticsBack)));
        assertThat(document.getStatistics()).containsOnly(documentStatisticsBack);
        assertThat(documentStatisticsBack.getDocument()).isEqualTo(document);

        document.setStatistics(new HashSet<>());
        assertThat(document.getStatistics()).doesNotContain(documentStatisticsBack);
        assertThat(documentStatisticsBack.getDocument()).isNull();
    }

    @Test
    void documentTypeTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentType documentTypeBack = getDocumentTypeRandomSampleGenerator();

        document.setDocumentType(documentTypeBack);
        assertThat(document.getDocumentType()).isEqualTo(documentTypeBack);

        document.documentType(null);
        assertThat(document.getDocumentType()).isNull();
    }

    @Test
    void folderTest() {
        Document document = getDocumentRandomSampleGenerator();
        MetaFolder metaFolderBack = getMetaFolderRandomSampleGenerator();

        document.setFolder(metaFolderBack);
        assertThat(document.getFolder()).isEqualTo(metaFolderBack);

        document.folder(null);
        assertThat(document.getFolder()).isNull();
    }
}
