package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.RedactionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RedactionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RedactionJobRepository extends JpaRepository<RedactionJob, Long> {}
