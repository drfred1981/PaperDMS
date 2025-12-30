package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationWebhookSubscription} and its DTO {@link NotificationWebhookSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationWebhookSubscriptionMapper
    extends EntityMapper<NotificationWebhookSubscriptionDTO, NotificationWebhookSubscription> {}
