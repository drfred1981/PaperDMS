package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.ComparisonJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.ComparisonJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComparisonJobMapperTest {

    private ComparisonJobMapper comparisonJobMapper;

    @BeforeEach
    void setUp() {
        comparisonJobMapper = new ComparisonJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getComparisonJobSample1();
        var actual = comparisonJobMapper.toEntity(comparisonJobMapper.toDto(expected));
        assertComparisonJobAllPropertiesEquals(expected, actual);
    }
}
