package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingDashboard} and its DTO {@link ReportingDashboardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingDashboardMapper extends EntityMapper<ReportingDashboardDTO, ReportingDashboard> {}
