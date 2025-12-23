package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.PerformanceMetric;
import fr.smartprod.paperdms.reporting.service.dto.PerformanceMetricDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PerformanceMetric} and its DTO {@link PerformanceMetricDTO}.
 */
@Mapper(componentModel = "spring")
public interface PerformanceMetricMapper extends EntityMapper<PerformanceMetricDTO, PerformanceMetric> {}
