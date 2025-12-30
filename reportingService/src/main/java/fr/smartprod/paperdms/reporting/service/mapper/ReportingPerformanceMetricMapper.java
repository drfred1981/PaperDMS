package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric;
import fr.smartprod.paperdms.reporting.service.dto.ReportingPerformanceMetricDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingPerformanceMetric} and its DTO {@link ReportingPerformanceMetricDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingPerformanceMetricMapper extends EntityMapper<ReportingPerformanceMetricDTO, ReportingPerformanceMetric> {}
