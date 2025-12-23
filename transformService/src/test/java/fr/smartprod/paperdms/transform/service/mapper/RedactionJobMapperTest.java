package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.RedactionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.RedactionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedactionJobMapperTest {

    private RedactionJobMapper redactionJobMapper;

    @BeforeEach
    void setUp() {
        redactionJobMapper = new RedactionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRedactionJobSample1();
        var actual = redactionJobMapper.toEntity(redactionJobMapper.toDto(expected));
        assertRedactionJobAllPropertiesEquals(expected, actual);
    }
}
