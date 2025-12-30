package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardDTO;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardWidgetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingDashboardWidget} and its DTO {@link ReportingDashboardWidgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingDashboardWidgetMapper extends EntityMapper<ReportingDashboardWidgetDTO, ReportingDashboardWidget> {
    @Mapping(target = "dashboar", source = "dashboar", qualifiedByName = "reportingDashboardId")
    ReportingDashboardWidgetDTO toDto(ReportingDashboardWidget s);

    @Named("reportingDashboardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportingDashboardDTO toDtoReportingDashboardId(ReportingDashboard reportingDashboard);
}
