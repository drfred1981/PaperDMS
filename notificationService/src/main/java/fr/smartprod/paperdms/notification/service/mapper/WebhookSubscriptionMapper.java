package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.WebhookSubscription;
import fr.smartprod.paperdms.notification.service.dto.WebhookSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WebhookSubscription} and its DTO {@link WebhookSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface WebhookSubscriptionMapper extends EntityMapper<WebhookSubscriptionDTO, WebhookSubscription> {}
