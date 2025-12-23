package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.SystemHealth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemHealth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemHealthRepository extends JpaRepository<SystemHealth, Long> {}
