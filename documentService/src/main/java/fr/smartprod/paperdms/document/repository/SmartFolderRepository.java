package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.SmartFolder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SmartFolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmartFolderRepository extends JpaRepository<SmartFolder, Long>, JpaSpecificationExecutor<SmartFolder> {}
