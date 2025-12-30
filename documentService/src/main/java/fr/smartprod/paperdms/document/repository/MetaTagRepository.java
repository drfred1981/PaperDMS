package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaTagRepository extends JpaRepository<MetaTag, Long>, JpaSpecificationExecutor<MetaTag> {}
