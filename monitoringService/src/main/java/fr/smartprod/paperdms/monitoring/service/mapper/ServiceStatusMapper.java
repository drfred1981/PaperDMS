package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.ServiceStatus;
import fr.smartprod.paperdms.monitoring.service.dto.ServiceStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceStatus} and its DTO {@link ServiceStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceStatusMapper extends EntityMapper<ServiceStatusDTO, ServiceStatus> {}
