package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportingExecution;
import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.service.dto.ReportingExecutionDTO;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingExecution} and its DTO {@link ReportingExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingExecutionMapper extends EntityMapper<ReportingExecutionDTO, ReportingExecution> {
    @Mapping(target = "scheduledReport", source = "scheduledReport", qualifiedByName = "reportingScheduledReportId")
    ReportingExecutionDTO toDto(ReportingExecution s);

    @Named("reportingScheduledReportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportingScheduledReportDTO toDtoReportingScheduledReportId(ReportingScheduledReport reportingScheduledReport);
}
