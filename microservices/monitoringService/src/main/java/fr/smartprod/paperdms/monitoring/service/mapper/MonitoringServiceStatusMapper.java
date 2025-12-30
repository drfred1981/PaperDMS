package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringServiceStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringServiceStatus} and its DTO {@link MonitoringServiceStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringServiceStatusMapper extends EntityMapper<MonitoringServiceStatusDTO, MonitoringServiceStatus> {}
