package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConversionJobCriteriaTest {

    @Test
    void newConversionJobCriteriaHasAllFiltersNullTest() {
        var conversionJobCriteria = new ConversionJobCriteria();
        assertThat(conversionJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void conversionJobCriteriaFluentMethodsCreatesFiltersTest() {
        var conversionJobCriteria = new ConversionJobCriteria();

        setAllFilters(conversionJobCriteria);

        assertThat(conversionJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void conversionJobCriteriaCopyCreatesNullFilterTest() {
        var conversionJobCriteria = new ConversionJobCriteria();
        var copy = conversionJobCriteria.copy();

        assertThat(conversionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(conversionJobCriteria)
        );
    }

    @Test
    void conversionJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var conversionJobCriteria = new ConversionJobCriteria();
        setAllFilters(conversionJobCriteria);

        var copy = conversionJobCriteria.copy();

        assertThat(conversionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(conversionJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var conversionJobCriteria = new ConversionJobCriteria();

        assertThat(conversionJobCriteria).hasToString("ConversionJobCriteria{}");
    }

    private static void setAllFilters(ConversionJobCriteria conversionJobCriteria) {
        conversionJobCriteria.id();
        conversionJobCriteria.documentId();
        conversionJobCriteria.documentSha256();
        conversionJobCriteria.sourceFormat();
        conversionJobCriteria.targetFormat();
        conversionJobCriteria.conversionEngine();
        conversionJobCriteria.outputS3Key();
        conversionJobCriteria.outputDocumentId();
        conversionJobCriteria.status();
        conversionJobCriteria.startDate();
        conversionJobCriteria.endDate();
        conversionJobCriteria.createdBy();
        conversionJobCriteria.createdDate();
        conversionJobCriteria.distinct();
    }

    private static Condition<ConversionJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getSourceFormat()) &&
                condition.apply(criteria.getTargetFormat()) &&
                condition.apply(criteria.getConversionEngine()) &&
                condition.apply(criteria.getOutputS3Key()) &&
                condition.apply(criteria.getOutputDocumentId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConversionJobCriteria> copyFiltersAre(
        ConversionJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getSourceFormat(), copy.getSourceFormat()) &&
                condition.apply(criteria.getTargetFormat(), copy.getTargetFormat()) &&
                condition.apply(criteria.getConversionEngine(), copy.getConversionEngine()) &&
                condition.apply(criteria.getOutputS3Key(), copy.getOutputS3Key()) &&
                condition.apply(criteria.getOutputDocumentId(), copy.getOutputDocumentId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
