package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookLog;
import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookLogDTO;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationWebhookLog} and its DTO {@link NotificationWebhookLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationWebhookLogMapper extends EntityMapper<NotificationWebhookLogDTO, NotificationWebhookLog> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "notificationWebhookSubscriptionId")
    NotificationWebhookLogDTO toDto(NotificationWebhookLog s);

    @Named("notificationWebhookSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NotificationWebhookSubscriptionDTO toDtoNotificationWebhookSubscriptionId(
        NotificationWebhookSubscription notificationWebhookSubscription
    );
}
