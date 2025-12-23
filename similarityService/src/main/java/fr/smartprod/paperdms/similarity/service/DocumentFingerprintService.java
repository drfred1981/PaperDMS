package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.service.dto.DocumentFingerprintDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.similarity.domain.DocumentFingerprint}.
 */
public interface DocumentFingerprintService {
    /**
     * Save a documentFingerprint.
     *
     * @param documentFingerprintDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentFingerprintDTO save(DocumentFingerprintDTO documentFingerprintDTO);

    /**
     * Updates a documentFingerprint.
     *
     * @param documentFingerprintDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentFingerprintDTO update(DocumentFingerprintDTO documentFingerprintDTO);

    /**
     * Partially updates a documentFingerprint.
     *
     * @param documentFingerprintDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentFingerprintDTO> partialUpdate(DocumentFingerprintDTO documentFingerprintDTO);

    /**
     * Get all the documentFingerprints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentFingerprintDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentFingerprint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentFingerprintDTO> findOne(Long id);

    /**
     * Delete the "id" documentFingerprint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentFingerprint corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentFingerprintDTO> search(String query, Pageable pageable);
}
