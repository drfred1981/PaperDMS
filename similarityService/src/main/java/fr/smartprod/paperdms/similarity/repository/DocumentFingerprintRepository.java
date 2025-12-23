package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.DocumentFingerprint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentFingerprint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentFingerprintRepository extends JpaRepository<DocumentFingerprint, Long> {}
