package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.Bookmark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bookmark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {}
