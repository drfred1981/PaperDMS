package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaBookmark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaBookmark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaBookmarkRepository extends JpaRepository<MetaBookmark, Long>, JpaSpecificationExecutor<MetaBookmark> {}
