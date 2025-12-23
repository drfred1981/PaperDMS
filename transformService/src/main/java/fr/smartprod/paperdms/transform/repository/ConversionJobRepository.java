package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.ConversionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConversionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversionJobRepository extends JpaRepository<ConversionJob, Long>, JpaSpecificationExecutor<ConversionJob> {}
