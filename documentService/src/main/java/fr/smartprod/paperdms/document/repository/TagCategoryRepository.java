package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.TagCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TagCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagCategoryRepository extends JpaRepository<TagCategory, Long>, JpaSpecificationExecutor<TagCategory> {}
