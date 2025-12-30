package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringSystemHealthDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringSystemHealth} and its DTO {@link MonitoringSystemHealthDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringSystemHealthMapper extends EntityMapper<MonitoringSystemHealthDTO, MonitoringSystemHealth> {}
