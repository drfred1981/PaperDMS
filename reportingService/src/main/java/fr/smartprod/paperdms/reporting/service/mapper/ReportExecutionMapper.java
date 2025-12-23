package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ReportExecution;
import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.service.dto.ReportExecutionDTO;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportExecution} and its DTO {@link ReportExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportExecutionMapper extends EntityMapper<ReportExecutionDTO, ReportExecution> {
    @Mapping(target = "scheduledReport", source = "scheduledReport", qualifiedByName = "scheduledReportId")
    ReportExecutionDTO toDto(ReportExecution s);

    @Named("scheduledReportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScheduledReportDTO toDtoScheduledReportId(ScheduledReport scheduledReport);
}
