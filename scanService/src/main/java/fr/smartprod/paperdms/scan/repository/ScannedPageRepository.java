package fr.smartprod.paperdms.scan.repository;

import fr.smartprod.paperdms.scan.domain.ScannedPage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScannedPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScannedPageRepository extends JpaRepository<ScannedPage, Long> {}
