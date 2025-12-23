package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.CompressionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.CompressionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompressionJobMapperTest {

    private CompressionJobMapper compressionJobMapper;

    @BeforeEach
    void setUp() {
        compressionJobMapper = new CompressionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompressionJobSample1();
        var actual = compressionJobMapper.toEntity(compressionJobMapper.toDto(expected));
        assertCompressionJobAllPropertiesEquals(expected, actual);
    }
}
