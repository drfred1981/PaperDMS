package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.TagCategory}.
 */
public interface TagCategoryService {
    /**
     * Save a tagCategory.
     *
     * @param tagCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    TagCategoryDTO save(TagCategoryDTO tagCategoryDTO);

    /**
     * Updates a tagCategory.
     *
     * @param tagCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    TagCategoryDTO update(TagCategoryDTO tagCategoryDTO);

    /**
     * Partially updates a tagCategory.
     *
     * @param tagCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagCategoryDTO> partialUpdate(TagCategoryDTO tagCategoryDTO);

    /**
     * Get the "id" tagCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" tagCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tagCategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagCategoryDTO> search(String query, Pageable pageable);
}
