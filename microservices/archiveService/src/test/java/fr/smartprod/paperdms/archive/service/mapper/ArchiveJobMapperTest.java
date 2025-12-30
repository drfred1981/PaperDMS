package fr.smartprod.paperdms.archive.service.mapper;

import static fr.smartprod.paperdms.archive.domain.ArchiveJobAsserts.*;
import static fr.smartprod.paperdms.archive.domain.ArchiveJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArchiveJobMapperTest {

    private ArchiveJobMapper archiveJobMapper;

    @BeforeEach
    void setUp() {
        archiveJobMapper = new ArchiveJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArchiveJobSample1();
        var actual = archiveJobMapper.toEntity(archiveJobMapper.toDto(expected));
        assertArchiveJobAllPropertiesEquals(expected, actual);
    }
}
