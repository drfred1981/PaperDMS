package fr.smartprod.paperdms.archive.repository;

import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArchiveJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchiveJobRepository extends JpaRepository<ArchiveJob, Long>, JpaSpecificationExecutor<ArchiveJob> {}
