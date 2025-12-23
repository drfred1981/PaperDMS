package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.BookmarkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.Bookmark}.
 */
public interface BookmarkService {
    /**
     * Save a bookmark.
     *
     * @param bookmarkDTO the entity to save.
     * @return the persisted entity.
     */
    BookmarkDTO save(BookmarkDTO bookmarkDTO);

    /**
     * Updates a bookmark.
     *
     * @param bookmarkDTO the entity to update.
     * @return the persisted entity.
     */
    BookmarkDTO update(BookmarkDTO bookmarkDTO);

    /**
     * Partially updates a bookmark.
     *
     * @param bookmarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookmarkDTO> partialUpdate(BookmarkDTO bookmarkDTO);

    /**
     * Get all the bookmarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookmarkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bookmark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookmarkDTO> findOne(Long id);

    /**
     * Delete the "id" bookmark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
