package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.ComparisonJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ComparisonJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComparisonJobRepository extends JpaRepository<ComparisonJob, Long> {}
