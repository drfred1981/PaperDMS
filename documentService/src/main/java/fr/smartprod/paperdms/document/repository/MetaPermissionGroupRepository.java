package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaPermissionGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaPermissionGroupRepository
    extends JpaRepository<MetaPermissionGroup, Long>, JpaSpecificationExecutor<MetaPermissionGroup> {}
