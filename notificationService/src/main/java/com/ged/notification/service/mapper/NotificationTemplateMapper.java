package com.ged.notification.service.mapper;

import com.ged.notification.domain.NotificationTemplate;
import com.ged.notification.service.dto.NotificationTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationTemplate} and its DTO {@link NotificationTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationTemplateMapper extends EntityMapper<NotificationTemplateDTO, NotificationTemplate> {}
