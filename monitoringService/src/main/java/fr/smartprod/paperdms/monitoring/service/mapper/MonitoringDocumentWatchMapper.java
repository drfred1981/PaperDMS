package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringDocumentWatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringDocumentWatch} and its DTO {@link MonitoringDocumentWatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringDocumentWatchMapper extends EntityMapper<MonitoringDocumentWatchDTO, MonitoringDocumentWatch> {}
