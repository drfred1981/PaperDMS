package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.service.dto.ImportMappingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportMapping}.
 */
public interface ImportMappingService {
    /**
     * Save a importMapping.
     *
     * @param importMappingDTO the entity to save.
     * @return the persisted entity.
     */
    ImportMappingDTO save(ImportMappingDTO importMappingDTO);

    /**
     * Updates a importMapping.
     *
     * @param importMappingDTO the entity to update.
     * @return the persisted entity.
     */
    ImportMappingDTO update(ImportMappingDTO importMappingDTO);

    /**
     * Partially updates a importMapping.
     *
     * @param importMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImportMappingDTO> partialUpdate(ImportMappingDTO importMappingDTO);

    /**
     * Get all the importMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImportMappingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" importMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImportMappingDTO> findOne(Long id);

    /**
     * Delete the "id" importMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
