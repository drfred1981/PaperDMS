package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentTypeFieldCriteriaTest {

    @Test
    void newDocumentTypeFieldCriteriaHasAllFiltersNullTest() {
        var documentTypeFieldCriteria = new DocumentTypeFieldCriteria();
        assertThat(documentTypeFieldCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentTypeFieldCriteriaFluentMethodsCreatesFiltersTest() {
        var documentTypeFieldCriteria = new DocumentTypeFieldCriteria();

        setAllFilters(documentTypeFieldCriteria);

        assertThat(documentTypeFieldCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentTypeFieldCriteriaCopyCreatesNullFilterTest() {
        var documentTypeFieldCriteria = new DocumentTypeFieldCriteria();
        var copy = documentTypeFieldCriteria.copy();

        assertThat(documentTypeFieldCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeFieldCriteria)
        );
    }

    @Test
    void documentTypeFieldCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentTypeFieldCriteria = new DocumentTypeFieldCriteria();
        setAllFilters(documentTypeFieldCriteria);

        var copy = documentTypeFieldCriteria.copy();

        assertThat(documentTypeFieldCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeFieldCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentTypeFieldCriteria = new DocumentTypeFieldCriteria();

        assertThat(documentTypeFieldCriteria).hasToString("DocumentTypeFieldCriteria{}");
    }

    private static void setAllFilters(DocumentTypeFieldCriteria documentTypeFieldCriteria) {
        documentTypeFieldCriteria.id();
        documentTypeFieldCriteria.fieldKey();
        documentTypeFieldCriteria.fieldLabel();
        documentTypeFieldCriteria.dataType();
        documentTypeFieldCriteria.isRequired();
        documentTypeFieldCriteria.isSearchable();
        documentTypeFieldCriteria.createdDate();
        documentTypeFieldCriteria.documentTypeId();
        documentTypeFieldCriteria.distinct();
    }

    private static Condition<DocumentTypeFieldCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFieldKey()) &&
                condition.apply(criteria.getFieldLabel()) &&
                condition.apply(criteria.getDataType()) &&
                condition.apply(criteria.getIsRequired()) &&
                condition.apply(criteria.getIsSearchable()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentTypeFieldCriteria> copyFiltersAre(
        DocumentTypeFieldCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFieldKey(), copy.getFieldKey()) &&
                condition.apply(criteria.getFieldLabel(), copy.getFieldLabel()) &&
                condition.apply(criteria.getDataType(), copy.getDataType()) &&
                condition.apply(criteria.getIsRequired(), copy.getIsRequired()) &&
                condition.apply(criteria.getIsSearchable(), copy.getIsSearchable()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
