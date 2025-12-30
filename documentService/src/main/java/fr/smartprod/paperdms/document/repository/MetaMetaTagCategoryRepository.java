package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaMetaTagCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaMetaTagCategoryRepository
    extends JpaRepository<MetaMetaTagCategory, Long>, JpaSpecificationExecutor<MetaMetaTagCategory> {}
