package fr.smartprod.paperdms.notification.repository;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationWebhookLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationWebhookLogRepository
    extends JpaRepository<NotificationWebhookLog, Long>, JpaSpecificationExecutor<NotificationWebhookLog> {}
