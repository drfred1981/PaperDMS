package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.DashboardAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.DashboardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DashboardMapperTest {

    private DashboardMapper dashboardMapper;

    @BeforeEach
    void setUp() {
        dashboardMapper = new DashboardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDashboardSample1();
        var actual = dashboardMapper.toEntity(dashboardMapper.toDto(expected));
        assertDashboardAllPropertiesEquals(expected, actual);
    }
}
