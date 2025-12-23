package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.ManualAsserts.*;
import static fr.smartprod.paperdms.business.domain.ManualTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualMapperTest {

    private ManualMapper manualMapper;

    @BeforeEach
    void setUp() {
        manualMapper = new ManualMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManualSample1();
        var actual = manualMapper.toEntity(manualMapper.toDto(expected));
        assertManualAllPropertiesEquals(expected, actual);
    }
}
