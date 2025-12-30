package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentTypeCriteriaTest {

    @Test
    void newDocumentTypeCriteriaHasAllFiltersNullTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        assertThat(documentTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();

        setAllFilters(documentTypeCriteria);

        assertThat(documentTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentTypeCriteriaCopyCreatesNullFilterTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        var copy = documentTypeCriteria.copy();

        assertThat(documentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeCriteria)
        );
    }

    @Test
    void documentTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        setAllFilters(documentTypeCriteria);

        var copy = documentTypeCriteria.copy();

        assertThat(documentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentTypeCriteria = new DocumentTypeCriteria();

        assertThat(documentTypeCriteria).hasToString("DocumentTypeCriteria{}");
    }

    private static void setAllFilters(DocumentTypeCriteria documentTypeCriteria) {
        documentTypeCriteria.id();
        documentTypeCriteria.name();
        documentTypeCriteria.code();
        documentTypeCriteria.icon();
        documentTypeCriteria.color();
        documentTypeCriteria.isActive();
        documentTypeCriteria.createdDate();
        documentTypeCriteria.createdBy();
        documentTypeCriteria.documentsId();
        documentTypeCriteria.documentTemplatesId();
        documentTypeCriteria.fieldsId();
        documentTypeCriteria.distinct();
    }

    private static Condition<DocumentTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getIcon()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getDocumentsId()) &&
                condition.apply(criteria.getDocumentTemplatesId()) &&
                condition.apply(criteria.getFieldsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentTypeCriteria> copyFiltersAre(
        DocumentTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getIcon(), copy.getIcon()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getDocumentsId(), copy.getDocumentsId()) &&
                condition.apply(criteria.getDocumentTemplatesId(), copy.getDocumentTemplatesId()) &&
                condition.apply(criteria.getFieldsId(), copy.getFieldsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
