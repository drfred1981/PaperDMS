package com.ged.notification.repository;

import com.ged.notification.domain.NotificationPreference;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationPreference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {}
