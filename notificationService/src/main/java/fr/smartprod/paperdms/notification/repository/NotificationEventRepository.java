package fr.smartprod.paperdms.notification.repository;

import fr.smartprod.paperdms.notification.domain.NotificationEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {}
