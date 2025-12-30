package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentCommentCriteriaTest {

    @Test
    void newDocumentCommentCriteriaHasAllFiltersNullTest() {
        var documentCommentCriteria = new DocumentCommentCriteria();
        assertThat(documentCommentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentCommentCriteriaFluentMethodsCreatesFiltersTest() {
        var documentCommentCriteria = new DocumentCommentCriteria();

        setAllFilters(documentCommentCriteria);

        assertThat(documentCommentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentCommentCriteriaCopyCreatesNullFilterTest() {
        var documentCommentCriteria = new DocumentCommentCriteria();
        var copy = documentCommentCriteria.copy();

        assertThat(documentCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentCommentCriteria)
        );
    }

    @Test
    void documentCommentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentCommentCriteria = new DocumentCommentCriteria();
        setAllFilters(documentCommentCriteria);

        var copy = documentCommentCriteria.copy();

        assertThat(documentCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentCommentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentCommentCriteria = new DocumentCommentCriteria();

        assertThat(documentCommentCriteria).hasToString("DocumentCommentCriteria{}");
    }

    private static void setAllFilters(DocumentCommentCriteria documentCommentCriteria) {
        documentCommentCriteria.id();
        documentCommentCriteria.pageNumber();
        documentCommentCriteria.isResolved();
        documentCommentCriteria.authorId();
        documentCommentCriteria.createdDate();
        documentCommentCriteria.repliesId();
        documentCommentCriteria.documentId();
        documentCommentCriteria.parentCommentId();
        documentCommentCriteria.distinct();
    }

    private static Condition<DocumentCommentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getIsResolved()) &&
                condition.apply(criteria.getAuthorId()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getRepliesId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getParentCommentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentCommentCriteria> copyFiltersAre(
        DocumentCommentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getIsResolved(), copy.getIsResolved()) &&
                condition.apply(criteria.getAuthorId(), copy.getAuthorId()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getRepliesId(), copy.getRepliesId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getParentCommentId(), copy.getParentCommentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
