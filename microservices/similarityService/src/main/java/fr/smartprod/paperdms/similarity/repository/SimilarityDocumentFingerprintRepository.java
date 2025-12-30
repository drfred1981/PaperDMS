package fr.smartprod.paperdms.similarity.repository;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarityDocumentFingerprint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarityDocumentFingerprintRepository
    extends JpaRepository<SimilarityDocumentFingerprint, Long>, JpaSpecificationExecutor<SimilarityDocumentFingerprint> {}
