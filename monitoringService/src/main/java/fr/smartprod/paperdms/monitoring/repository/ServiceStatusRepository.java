package fr.smartprod.paperdms.monitoring.repository;

import fr.smartprod.paperdms.monitoring.domain.ServiceStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {}
