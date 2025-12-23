package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MaintenanceTask;
import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaintenanceTask} and its DTO {@link MaintenanceTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceTaskMapper extends EntityMapper<MaintenanceTaskDTO, MaintenanceTask> {}
