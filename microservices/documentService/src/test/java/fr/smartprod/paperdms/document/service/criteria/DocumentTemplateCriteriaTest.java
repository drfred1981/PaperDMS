package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentTemplateCriteriaTest {

    @Test
    void newDocumentTemplateCriteriaHasAllFiltersNullTest() {
        var documentTemplateCriteria = new DocumentTemplateCriteria();
        assertThat(documentTemplateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var documentTemplateCriteria = new DocumentTemplateCriteria();

        setAllFilters(documentTemplateCriteria);

        assertThat(documentTemplateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentTemplateCriteriaCopyCreatesNullFilterTest() {
        var documentTemplateCriteria = new DocumentTemplateCriteria();
        var copy = documentTemplateCriteria.copy();

        assertThat(documentTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTemplateCriteria)
        );
    }

    @Test
    void documentTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentTemplateCriteria = new DocumentTemplateCriteria();
        setAllFilters(documentTemplateCriteria);

        var copy = documentTemplateCriteria.copy();

        assertThat(documentTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentTemplateCriteria = new DocumentTemplateCriteria();

        assertThat(documentTemplateCriteria).hasToString("DocumentTemplateCriteria{}");
    }

    private static void setAllFilters(DocumentTemplateCriteria documentTemplateCriteria) {
        documentTemplateCriteria.id();
        documentTemplateCriteria.name();
        documentTemplateCriteria.templateSha256();
        documentTemplateCriteria.templateS3Key();
        documentTemplateCriteria.isActive();
        documentTemplateCriteria.createdBy();
        documentTemplateCriteria.createdDate();
        documentTemplateCriteria.documentTypeId();
        documentTemplateCriteria.distinct();
    }

    private static Condition<DocumentTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTemplateSha256()) &&
                condition.apply(criteria.getTemplateS3Key()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentTemplateCriteria> copyFiltersAre(
        DocumentTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTemplateSha256(), copy.getTemplateSha256()) &&
                condition.apply(criteria.getTemplateS3Key(), copy.getTemplateS3Key()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
