package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.MergeJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MergeJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MergeJobRepository extends JpaRepository<MergeJob, Long> {}
