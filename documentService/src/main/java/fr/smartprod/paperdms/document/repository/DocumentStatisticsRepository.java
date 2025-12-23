package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentStatisticsRepository extends JpaRepository<DocumentStatistics, Long> {}
