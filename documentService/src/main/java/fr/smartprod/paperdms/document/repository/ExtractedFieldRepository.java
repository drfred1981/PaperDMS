package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.ExtractedField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExtractedField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtractedFieldRepository extends JpaRepository<ExtractedField, Long> {}
