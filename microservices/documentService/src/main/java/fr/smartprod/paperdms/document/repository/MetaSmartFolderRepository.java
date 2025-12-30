package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaSmartFolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaSmartFolderRepository extends JpaRepository<MetaSmartFolder, Long>, JpaSpecificationExecutor<MetaSmartFolder> {}
