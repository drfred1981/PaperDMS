package fr.smartprod.paperdms.transform.repository;

import fr.smartprod.paperdms.transform.domain.TransformRedactionJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransformRedactionJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformRedactionJobRepository
    extends JpaRepository<TransformRedactionJob, Long>, JpaSpecificationExecutor<TransformRedactionJob> {}
