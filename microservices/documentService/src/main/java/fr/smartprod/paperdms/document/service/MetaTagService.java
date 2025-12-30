package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.repository.MetaTagRepository;
import fr.smartprod.paperdms.document.repository.search.MetaTagSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaTag}.
 */
@Service
@Transactional
public class MetaTagService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTagService.class);

    private final MetaTagRepository metaTagRepository;

    private final MetaTagMapper metaTagMapper;

    private final MetaTagSearchRepository metaTagSearchRepository;

    public MetaTagService(
        MetaTagRepository metaTagRepository,
        MetaTagMapper metaTagMapper,
        MetaTagSearchRepository metaTagSearchRepository
    ) {
        this.metaTagRepository = metaTagRepository;
        this.metaTagMapper = metaTagMapper;
        this.metaTagSearchRepository = metaTagSearchRepository;
    }

    /**
     * Save a metaTag.
     *
     * @param metaTagDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaTagDTO save(MetaTagDTO metaTagDTO) {
        LOG.debug("Request to save MetaTag : {}", metaTagDTO);
        MetaTag metaTag = metaTagMapper.toEntity(metaTagDTO);
        metaTag = metaTagRepository.save(metaTag);
        metaTagSearchRepository.index(metaTag);
        return metaTagMapper.toDto(metaTag);
    }

    /**
     * Update a metaTag.
     *
     * @param metaTagDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaTagDTO update(MetaTagDTO metaTagDTO) {
        LOG.debug("Request to update MetaTag : {}", metaTagDTO);
        MetaTag metaTag = metaTagMapper.toEntity(metaTagDTO);
        metaTag = metaTagRepository.save(metaTag);
        metaTagSearchRepository.index(metaTag);
        return metaTagMapper.toDto(metaTag);
    }

    /**
     * Partially update a metaTag.
     *
     * @param metaTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaTagDTO> partialUpdate(MetaTagDTO metaTagDTO) {
        LOG.debug("Request to partially update MetaTag : {}", metaTagDTO);

        return metaTagRepository
            .findById(metaTagDTO.getId())
            .map(existingMetaTag -> {
                metaTagMapper.partialUpdate(existingMetaTag, metaTagDTO);

                return existingMetaTag;
            })
            .map(metaTagRepository::save)
            .map(savedMetaTag -> {
                metaTagSearchRepository.index(savedMetaTag);
                return savedMetaTag;
            })
            .map(metaTagMapper::toDto);
    }

    /**
     * Get one metaTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaTagDTO> findOne(Long id) {
        LOG.debug("Request to get MetaTag : {}", id);
        return metaTagRepository.findById(id).map(metaTagMapper::toDto);
    }

    /**
     * Delete the metaTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaTag : {}", id);
        metaTagRepository.deleteById(id);
        metaTagSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaTag corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaTagDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaTags for query {}", query);
        return metaTagSearchRepository.search(query, pageable).map(metaTagMapper::toDto);
    }
}
