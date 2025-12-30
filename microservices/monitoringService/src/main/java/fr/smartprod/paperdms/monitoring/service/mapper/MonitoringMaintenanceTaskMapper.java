package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringMaintenanceTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringMaintenanceTask} and its DTO {@link MonitoringMaintenanceTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringMaintenanceTaskMapper extends EntityMapper<MonitoringMaintenanceTaskDTO, MonitoringMaintenanceTask> {}
