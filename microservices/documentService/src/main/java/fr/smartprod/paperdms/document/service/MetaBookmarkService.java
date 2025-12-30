package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaBookmark;
import fr.smartprod.paperdms.document.repository.MetaBookmarkRepository;
import fr.smartprod.paperdms.document.repository.search.MetaBookmarkSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaBookmarkDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaBookmarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaBookmark}.
 */
@Service
@Transactional
public class MetaBookmarkService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaBookmarkService.class);

    private final MetaBookmarkRepository metaBookmarkRepository;

    private final MetaBookmarkMapper metaBookmarkMapper;

    private final MetaBookmarkSearchRepository metaBookmarkSearchRepository;

    public MetaBookmarkService(
        MetaBookmarkRepository metaBookmarkRepository,
        MetaBookmarkMapper metaBookmarkMapper,
        MetaBookmarkSearchRepository metaBookmarkSearchRepository
    ) {
        this.metaBookmarkRepository = metaBookmarkRepository;
        this.metaBookmarkMapper = metaBookmarkMapper;
        this.metaBookmarkSearchRepository = metaBookmarkSearchRepository;
    }

    /**
     * Save a metaBookmark.
     *
     * @param metaBookmarkDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaBookmarkDTO save(MetaBookmarkDTO metaBookmarkDTO) {
        LOG.debug("Request to save MetaBookmark : {}", metaBookmarkDTO);
        MetaBookmark metaBookmark = metaBookmarkMapper.toEntity(metaBookmarkDTO);
        metaBookmark = metaBookmarkRepository.save(metaBookmark);
        metaBookmarkSearchRepository.index(metaBookmark);
        return metaBookmarkMapper.toDto(metaBookmark);
    }

    /**
     * Update a metaBookmark.
     *
     * @param metaBookmarkDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaBookmarkDTO update(MetaBookmarkDTO metaBookmarkDTO) {
        LOG.debug("Request to update MetaBookmark : {}", metaBookmarkDTO);
        MetaBookmark metaBookmark = metaBookmarkMapper.toEntity(metaBookmarkDTO);
        metaBookmark = metaBookmarkRepository.save(metaBookmark);
        metaBookmarkSearchRepository.index(metaBookmark);
        return metaBookmarkMapper.toDto(metaBookmark);
    }

    /**
     * Partially update a metaBookmark.
     *
     * @param metaBookmarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaBookmarkDTO> partialUpdate(MetaBookmarkDTO metaBookmarkDTO) {
        LOG.debug("Request to partially update MetaBookmark : {}", metaBookmarkDTO);

        return metaBookmarkRepository
            .findById(metaBookmarkDTO.getId())
            .map(existingMetaBookmark -> {
                metaBookmarkMapper.partialUpdate(existingMetaBookmark, metaBookmarkDTO);

                return existingMetaBookmark;
            })
            .map(metaBookmarkRepository::save)
            .map(savedMetaBookmark -> {
                metaBookmarkSearchRepository.index(savedMetaBookmark);
                return savedMetaBookmark;
            })
            .map(metaBookmarkMapper::toDto);
    }

    /**
     * Get one metaBookmark by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaBookmarkDTO> findOne(Long id) {
        LOG.debug("Request to get MetaBookmark : {}", id);
        return metaBookmarkRepository.findById(id).map(metaBookmarkMapper::toDto);
    }

    /**
     * Delete the metaBookmark by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaBookmark : {}", id);
        metaBookmarkRepository.deleteById(id);
        metaBookmarkSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaBookmark corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaBookmarkDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaBookmarks for query {}", query);
        return metaBookmarkSearchRepository.search(query, pageable).map(metaBookmarkMapper::toDto);
    }
}
