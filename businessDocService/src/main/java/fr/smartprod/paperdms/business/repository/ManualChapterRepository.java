package fr.smartprod.paperdms.business.repository;

import fr.smartprod.paperdms.business.domain.ManualChapter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ManualChapter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualChapterRepository extends JpaRepository<ManualChapter, Long> {}
