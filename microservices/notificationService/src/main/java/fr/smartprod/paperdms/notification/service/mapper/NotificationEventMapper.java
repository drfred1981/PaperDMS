package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.NotificationEvent;
import fr.smartprod.paperdms.notification.service.dto.NotificationEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationEvent} and its DTO {@link NotificationEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationEventMapper extends EntityMapper<NotificationEventDTO, NotificationEvent> {}
