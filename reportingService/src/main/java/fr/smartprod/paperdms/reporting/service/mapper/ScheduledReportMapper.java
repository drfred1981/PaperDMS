package fr.smartprod.paperdms.reporting.service.mapper;

import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScheduledReport} and its DTO {@link ScheduledReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduledReportMapper extends EntityMapper<ScheduledReportDTO, ScheduledReport> {}
