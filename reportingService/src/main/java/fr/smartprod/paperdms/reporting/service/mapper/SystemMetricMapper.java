package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.SystemMetric;
import fr.smartprod.paperdms.reporting.service.dto.SystemMetricDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemMetric} and its DTO {@link SystemMetricDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemMetricMapper extends EntityMapper<SystemMetricDTO, SystemMetric> {}
