package fr.smartprod.paperdms.business.repository;

import fr.smartprod.paperdms.business.domain.Manual;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Manual entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualRepository extends JpaRepository<Manual, Long>, JpaSpecificationExecutor<Manual> {}
