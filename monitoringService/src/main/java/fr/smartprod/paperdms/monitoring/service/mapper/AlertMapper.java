package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.Alert;
import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.service.dto.AlertDTO;
import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {
    @Mapping(target = "alertRule", source = "alertRule", qualifiedByName = "alertRuleId")
    AlertDTO toDto(Alert s);

    @Named("alertRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlertRuleDTO toDtoAlertRuleId(AlertRule alertRule);
}
