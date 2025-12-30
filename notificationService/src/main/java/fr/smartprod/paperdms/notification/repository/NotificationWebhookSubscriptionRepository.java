package fr.smartprod.paperdms.notification.repository;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationWebhookSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationWebhookSubscriptionRepository
    extends JpaRepository<NotificationWebhookSubscription, Long>, JpaSpecificationExecutor<NotificationWebhookSubscription> {}
