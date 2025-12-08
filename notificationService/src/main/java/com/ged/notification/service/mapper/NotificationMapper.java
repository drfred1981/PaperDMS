package com.ged.notification.service.mapper;

import com.ged.notification.domain.Notification;
import com.ged.notification.domain.NotificationTemplate;
import com.ged.notification.service.dto.NotificationDTO;
import com.ged.notification.service.dto.NotificationTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "template", source = "template", qualifiedByName = "notificationTemplateId")
    NotificationDTO toDto(Notification s);

    @Named("notificationTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NotificationTemplateDTO toDtoNotificationTemplateId(NotificationTemplate notificationTemplate);
}
