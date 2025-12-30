package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitoringAlertRule} and its DTO {@link MonitoringAlertRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonitoringAlertRuleMapper extends EntityMapper<MonitoringAlertRuleDTO, MonitoringAlertRule> {}
