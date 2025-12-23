package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.CompressionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompressionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompressionJobRepository extends JpaRepository<CompressionJob, Long> {}
