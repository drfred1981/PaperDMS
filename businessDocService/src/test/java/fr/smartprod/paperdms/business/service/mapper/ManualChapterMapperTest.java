package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.ManualChapterAsserts.*;
import static fr.smartprod.paperdms.business.domain.ManualChapterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualChapterMapperTest {

    private ManualChapterMapper manualChapterMapper;

    @BeforeEach
    void setUp() {
        manualChapterMapper = new ManualChapterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManualChapterSample1();
        var actual = manualChapterMapper.toEntity(manualChapterMapper.toDto(expected));
        assertManualChapterAllPropertiesEquals(expected, actual);
    }
}
