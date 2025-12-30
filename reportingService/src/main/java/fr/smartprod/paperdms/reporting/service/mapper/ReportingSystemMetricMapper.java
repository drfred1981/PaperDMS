package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric;
import fr.smartprod.paperdms.reporting.service.dto.ReportingSystemMetricDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingSystemMetric} and its DTO {@link ReportingSystemMetricDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingSystemMetricMapper extends EntityMapper<ReportingSystemMetricDTO, ReportingSystemMetric> {}
