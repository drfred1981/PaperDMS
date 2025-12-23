package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlertRule} and its DTO {@link AlertRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertRuleMapper extends EntityMapper<AlertRuleDTO, AlertRule> {}
