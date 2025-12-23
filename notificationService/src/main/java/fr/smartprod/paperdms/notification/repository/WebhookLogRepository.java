package fr.smartprod.paperdms.notification.repository;

import fr.smartprod.paperdms.notification.domain.WebhookLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WebhookLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebhookLogRepository extends JpaRepository<WebhookLog, Long> {}
