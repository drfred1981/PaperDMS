package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.MergeJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.MergeJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MergeJobMapperTest {

    private MergeJobMapper mergeJobMapper;

    @BeforeEach
    void setUp() {
        mergeJobMapper = new MergeJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMergeJobSample1();
        var actual = mergeJobMapper.toEntity(mergeJobMapper.toDto(expected));
        assertMergeJobAllPropertiesEquals(expected, actual);
    }
}
