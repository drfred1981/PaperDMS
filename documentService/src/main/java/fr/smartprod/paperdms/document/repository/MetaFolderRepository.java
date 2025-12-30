package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaFolder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaFolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaFolderRepository extends JpaRepository<MetaFolder, Long>, JpaSpecificationExecutor<MetaFolder> {}
