package fr.smartprod.paperdms.reporting.service.mapper;

import static fr.smartprod.paperdms.reporting.domain.DashboardWidgetAsserts.*;
import static fr.smartprod.paperdms.reporting.domain.DashboardWidgetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DashboardWidgetMapperTest {

    private DashboardWidgetMapper dashboardWidgetMapper;

    @BeforeEach
    void setUp() {
        dashboardWidgetMapper = new DashboardWidgetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDashboardWidgetSample1();
        var actual = dashboardWidgetMapper.toEntity(dashboardWidgetMapper.toDto(expected));
        assertDashboardWidgetAllPropertiesEquals(expected, actual);
    }
}
