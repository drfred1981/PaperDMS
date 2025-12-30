package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.NotificationTemplate;
import fr.smartprod.paperdms.notification.service.dto.NotificationTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationTemplate} and its DTO {@link NotificationTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationTemplateMapper extends EntityMapper<NotificationTemplateDTO, NotificationTemplate> {}
