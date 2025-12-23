package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.SystemHealth;
import fr.smartprod.paperdms.monitoring.service.dto.SystemHealthDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemHealth} and its DTO {@link SystemHealthDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemHealthMapper extends EntityMapper<SystemHealthDTO, SystemHealth> {}
