package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.WebhookLog;
import fr.smartprod.paperdms.notification.domain.WebhookSubscription;
import fr.smartprod.paperdms.notification.service.dto.WebhookLogDTO;
import fr.smartprod.paperdms.notification.service.dto.WebhookSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WebhookLog} and its DTO {@link WebhookLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface WebhookLogMapper extends EntityMapper<WebhookLogDTO, WebhookLog> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "webhookSubscriptionId")
    WebhookLogDTO toDto(WebhookLog s);

    @Named("webhookSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WebhookSubscriptionDTO toDtoWebhookSubscriptionId(WebhookSubscription webhookSubscription);
}
