package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.PermissionGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PermissionGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long> {}
