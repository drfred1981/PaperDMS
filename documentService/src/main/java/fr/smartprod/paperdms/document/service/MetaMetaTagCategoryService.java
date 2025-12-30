package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.repository.MetaMetaTagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.MetaMetaTagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaMetaTagCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaMetaTagCategory}.
 */
@Service
@Transactional
public class MetaMetaTagCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaMetaTagCategoryService.class);

    private final MetaMetaTagCategoryRepository metaMetaTagCategoryRepository;

    private final MetaMetaTagCategoryMapper metaMetaTagCategoryMapper;

    private final MetaMetaTagCategorySearchRepository metaMetaTagCategorySearchRepository;

    public MetaMetaTagCategoryService(
        MetaMetaTagCategoryRepository metaMetaTagCategoryRepository,
        MetaMetaTagCategoryMapper metaMetaTagCategoryMapper,
        MetaMetaTagCategorySearchRepository metaMetaTagCategorySearchRepository
    ) {
        this.metaMetaTagCategoryRepository = metaMetaTagCategoryRepository;
        this.metaMetaTagCategoryMapper = metaMetaTagCategoryMapper;
        this.metaMetaTagCategorySearchRepository = metaMetaTagCategorySearchRepository;
    }

    /**
     * Save a metaMetaTagCategory.
     *
     * @param metaMetaTagCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaMetaTagCategoryDTO save(MetaMetaTagCategoryDTO metaMetaTagCategoryDTO) {
        LOG.debug("Request to save MetaMetaTagCategory : {}", metaMetaTagCategoryDTO);
        MetaMetaTagCategory metaMetaTagCategory = metaMetaTagCategoryMapper.toEntity(metaMetaTagCategoryDTO);
        metaMetaTagCategory = metaMetaTagCategoryRepository.save(metaMetaTagCategory);
        metaMetaTagCategorySearchRepository.index(metaMetaTagCategory);
        return metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);
    }

    /**
     * Update a metaMetaTagCategory.
     *
     * @param metaMetaTagCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaMetaTagCategoryDTO update(MetaMetaTagCategoryDTO metaMetaTagCategoryDTO) {
        LOG.debug("Request to update MetaMetaTagCategory : {}", metaMetaTagCategoryDTO);
        MetaMetaTagCategory metaMetaTagCategory = metaMetaTagCategoryMapper.toEntity(metaMetaTagCategoryDTO);
        metaMetaTagCategory = metaMetaTagCategoryRepository.save(metaMetaTagCategory);
        metaMetaTagCategorySearchRepository.index(metaMetaTagCategory);
        return metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);
    }

    /**
     * Partially update a metaMetaTagCategory.
     *
     * @param metaMetaTagCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaMetaTagCategoryDTO> partialUpdate(MetaMetaTagCategoryDTO metaMetaTagCategoryDTO) {
        LOG.debug("Request to partially update MetaMetaTagCategory : {}", metaMetaTagCategoryDTO);

        return metaMetaTagCategoryRepository
            .findById(metaMetaTagCategoryDTO.getId())
            .map(existingMetaMetaTagCategory -> {
                metaMetaTagCategoryMapper.partialUpdate(existingMetaMetaTagCategory, metaMetaTagCategoryDTO);

                return existingMetaMetaTagCategory;
            })
            .map(metaMetaTagCategoryRepository::save)
            .map(savedMetaMetaTagCategory -> {
                metaMetaTagCategorySearchRepository.index(savedMetaMetaTagCategory);
                return savedMetaMetaTagCategory;
            })
            .map(metaMetaTagCategoryMapper::toDto);
    }

    /**
     * Get one metaMetaTagCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaMetaTagCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get MetaMetaTagCategory : {}", id);
        return metaMetaTagCategoryRepository.findById(id).map(metaMetaTagCategoryMapper::toDto);
    }

    /**
     * Delete the metaMetaTagCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaMetaTagCategory : {}", id);
        metaMetaTagCategoryRepository.deleteById(id);
        metaMetaTagCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaMetaTagCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaMetaTagCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaMetaTagCategories for query {}", query);
        return metaMetaTagCategorySearchRepository.search(query, pageable).map(metaMetaTagCategoryMapper::toDto);
    }
}
