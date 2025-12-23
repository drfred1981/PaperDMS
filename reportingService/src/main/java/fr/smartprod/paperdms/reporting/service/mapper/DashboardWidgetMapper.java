package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.domain.DashboardWidget;
import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import fr.smartprod.paperdms.reporting.service.dto.DashboardWidgetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DashboardWidget} and its DTO {@link DashboardWidgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface DashboardWidgetMapper extends EntityMapper<DashboardWidgetDTO, DashboardWidget> {
    @Mapping(target = "dashboard", source = "dashboard", qualifiedByName = "dashboardId")
    DashboardWidgetDTO toDto(DashboardWidget s);

    @Named("dashboardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DashboardDTO toDtoDashboardId(Dashboard dashboard);
}
