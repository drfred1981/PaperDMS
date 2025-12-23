package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentCommentTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentComment.class);
        DocumentComment documentComment1 = getDocumentCommentSample1();
        DocumentComment documentComment2 = new DocumentComment();
        assertThat(documentComment1).isNotEqualTo(documentComment2);

        documentComment2.setId(documentComment1.getId());
        assertThat(documentComment1).isEqualTo(documentComment2);

        documentComment2 = getDocumentCommentSample2();
        assertThat(documentComment1).isNotEqualTo(documentComment2);
    }

    @Test
    void repliesTest() {
        DocumentComment documentComment = getDocumentCommentRandomSampleGenerator();
        DocumentComment documentCommentBack = getDocumentCommentRandomSampleGenerator();

        documentComment.addReplies(documentCommentBack);
        assertThat(documentComment.getReplies()).containsOnly(documentCommentBack);
        assertThat(documentCommentBack.getParentComment()).isEqualTo(documentComment);

        documentComment.removeReplies(documentCommentBack);
        assertThat(documentComment.getReplies()).doesNotContain(documentCommentBack);
        assertThat(documentCommentBack.getParentComment()).isNull();

        documentComment.replies(new HashSet<>(Set.of(documentCommentBack)));
        assertThat(documentComment.getReplies()).containsOnly(documentCommentBack);
        assertThat(documentCommentBack.getParentComment()).isEqualTo(documentComment);

        documentComment.setReplies(new HashSet<>());
        assertThat(documentComment.getReplies()).doesNotContain(documentCommentBack);
        assertThat(documentCommentBack.getParentComment()).isNull();
    }

    @Test
    void parentCommentTest() {
        DocumentComment documentComment = getDocumentCommentRandomSampleGenerator();
        DocumentComment documentCommentBack = getDocumentCommentRandomSampleGenerator();

        documentComment.setParentComment(documentCommentBack);
        assertThat(documentComment.getParentComment()).isEqualTo(documentCommentBack);

        documentComment.parentComment(null);
        assertThat(documentComment.getParentComment()).isNull();
    }
}
