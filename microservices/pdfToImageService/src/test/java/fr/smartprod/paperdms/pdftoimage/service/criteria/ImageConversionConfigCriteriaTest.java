package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageConversionConfigCriteriaTest {

    @Test
    void newImageConversionConfigCriteriaHasAllFiltersNullTest() {
        var imageConversionConfigCriteria = new ImageConversionConfigCriteria();
        assertThat(imageConversionConfigCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imageConversionConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var imageConversionConfigCriteria = new ImageConversionConfigCriteria();

        setAllFilters(imageConversionConfigCriteria);

        assertThat(imageConversionConfigCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imageConversionConfigCriteriaCopyCreatesNullFilterTest() {
        var imageConversionConfigCriteria = new ImageConversionConfigCriteria();
        var copy = imageConversionConfigCriteria.copy();

        assertThat(imageConversionConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionConfigCriteria)
        );
    }

    @Test
    void imageConversionConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageConversionConfigCriteria = new ImageConversionConfigCriteria();
        setAllFilters(imageConversionConfigCriteria);

        var copy = imageConversionConfigCriteria.copy();

        assertThat(imageConversionConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageConversionConfigCriteria = new ImageConversionConfigCriteria();

        assertThat(imageConversionConfigCriteria).hasToString("ImageConversionConfigCriteria{}");
    }

    private static void setAllFilters(ImageConversionConfigCriteria imageConversionConfigCriteria) {
        imageConversionConfigCriteria.id();
        imageConversionConfigCriteria.configName();
        imageConversionConfigCriteria.description();
        imageConversionConfigCriteria.defaultQuality();
        imageConversionConfigCriteria.defaultFormat();
        imageConversionConfigCriteria.defaultDpi();
        imageConversionConfigCriteria.defaultConversionType();
        imageConversionConfigCriteria.defaultPriority();
        imageConversionConfigCriteria.isActive();
        imageConversionConfigCriteria.isDefault();
        imageConversionConfigCriteria.createdAt();
        imageConversionConfigCriteria.updatedAt();
        imageConversionConfigCriteria.distinct();
    }

    private static Condition<ImageConversionConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getConfigName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDefaultQuality()) &&
                condition.apply(criteria.getDefaultFormat()) &&
                condition.apply(criteria.getDefaultDpi()) &&
                condition.apply(criteria.getDefaultConversionType()) &&
                condition.apply(criteria.getDefaultPriority()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageConversionConfigCriteria> copyFiltersAre(
        ImageConversionConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getConfigName(), copy.getConfigName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDefaultQuality(), copy.getDefaultQuality()) &&
                condition.apply(criteria.getDefaultFormat(), copy.getDefaultFormat()) &&
                condition.apply(criteria.getDefaultDpi(), copy.getDefaultDpi()) &&
                condition.apply(criteria.getDefaultConversionType(), copy.getDefaultConversionType()) &&
                condition.apply(criteria.getDefaultPriority(), copy.getDefaultPriority()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
