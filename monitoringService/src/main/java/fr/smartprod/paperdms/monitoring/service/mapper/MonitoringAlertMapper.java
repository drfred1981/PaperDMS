package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlert;
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertDTO;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringAlert} and its DTO {@link MonitoringAlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringAlertMapper extends EntityMapper<MonitoringAlertDTO, MonitoringAlert> {
    @Mapping(target = "alertRule", source = "alertRule", qualifiedByName = "monitoringAlertRuleId")
    MonitoringAlertDTO toDto(MonitoringAlert s);

    @Named("monitoringAlertRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MonitoringAlertRuleDTO toDtoMonitoringAlertRuleId(MonitoringAlertRule monitoringAlertRule);
}
