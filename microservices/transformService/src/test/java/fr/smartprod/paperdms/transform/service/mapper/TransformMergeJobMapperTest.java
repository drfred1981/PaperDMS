package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.TransformMergeJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.TransformMergeJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformMergeJobMapperTest {

    private TransformMergeJobMapper transformMergeJobMapper;

    @BeforeEach
    void setUp() {
        transformMergeJobMapper = new TransformMergeJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransformMergeJobSample1();
        var actual = transformMergeJobMapper.toEntity(transformMergeJobMapper.toDto(expected));
        assertTransformMergeJobAllPropertiesEquals(expected, actual);
    }
}
