package com.ged.notification.service.mapper;

import com.ged.notification.domain.NotificationEvent;
import com.ged.notification.service.dto.NotificationEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationEvent} and its DTO {@link NotificationEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationEventMapper extends EntityMapper<NotificationEventDTO, NotificationEvent> {}
