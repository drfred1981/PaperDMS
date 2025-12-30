package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.TransformRedactionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.TransformRedactionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformRedactionJobMapperTest {

    private TransformRedactionJobMapper transformRedactionJobMapper;

    @BeforeEach
    void setUp() {
        transformRedactionJobMapper = new TransformRedactionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransformRedactionJobSample1();
        var actual = transformRedactionJobMapper.toEntity(transformRedactionJobMapper.toDto(expected));
        assertTransformRedactionJobAllPropertiesEquals(expected, actual);
    }
}
