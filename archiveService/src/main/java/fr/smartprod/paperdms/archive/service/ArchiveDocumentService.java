package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument}.
 */
public interface ArchiveDocumentService {
    /**
     * Save a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    ArchiveDocumentDTO save(ArchiveDocumentDTO archiveDocumentDTO);

    /**
     * Updates a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    ArchiveDocumentDTO update(ArchiveDocumentDTO archiveDocumentDTO);

    /**
     * Partially updates a archiveDocument.
     *
     * @param archiveDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArchiveDocumentDTO> partialUpdate(ArchiveDocumentDTO archiveDocumentDTO);

    /**
     * Get all the archiveDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArchiveDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" archiveDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArchiveDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" archiveDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
