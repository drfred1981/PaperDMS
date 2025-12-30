package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingScheduledReport} and its DTO {@link ReportingScheduledReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingScheduledReportMapper extends EntityMapper<ReportingScheduledReportDTO, ReportingScheduledReport> {}
