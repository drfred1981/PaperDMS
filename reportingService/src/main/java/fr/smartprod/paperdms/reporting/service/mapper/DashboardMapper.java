package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dashboard} and its DTO {@link DashboardDTO}.
 */
@Mapper(componentModel = "spring")
public interface DashboardMapper extends EntityMapper<DashboardDTO, Dashboard> {}
