package fr.smartprod.paperdms.scan.repository;

import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScannerConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScannerConfigurationRepository extends JpaRepository<ScannerConfiguration, Long> {}
