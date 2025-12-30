package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.repository.MetaPermissionGroupRepository;
import fr.smartprod.paperdms.document.repository.search.MetaPermissionGroupSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaPermissionGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaPermissionGroup}.
 */
@Service
@Transactional
public class MetaPermissionGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaPermissionGroupService.class);

    private final MetaPermissionGroupRepository metaPermissionGroupRepository;

    private final MetaPermissionGroupMapper metaPermissionGroupMapper;

    private final MetaPermissionGroupSearchRepository metaPermissionGroupSearchRepository;

    public MetaPermissionGroupService(
        MetaPermissionGroupRepository metaPermissionGroupRepository,
        MetaPermissionGroupMapper metaPermissionGroupMapper,
        MetaPermissionGroupSearchRepository metaPermissionGroupSearchRepository
    ) {
        this.metaPermissionGroupRepository = metaPermissionGroupRepository;
        this.metaPermissionGroupMapper = metaPermissionGroupMapper;
        this.metaPermissionGroupSearchRepository = metaPermissionGroupSearchRepository;
    }

    /**
     * Save a metaPermissionGroup.
     *
     * @param metaPermissionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaPermissionGroupDTO save(MetaPermissionGroupDTO metaPermissionGroupDTO) {
        LOG.debug("Request to save MetaPermissionGroup : {}", metaPermissionGroupDTO);
        MetaPermissionGroup metaPermissionGroup = metaPermissionGroupMapper.toEntity(metaPermissionGroupDTO);
        metaPermissionGroup = metaPermissionGroupRepository.save(metaPermissionGroup);
        metaPermissionGroupSearchRepository.index(metaPermissionGroup);
        return metaPermissionGroupMapper.toDto(metaPermissionGroup);
    }

    /**
     * Update a metaPermissionGroup.
     *
     * @param metaPermissionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaPermissionGroupDTO update(MetaPermissionGroupDTO metaPermissionGroupDTO) {
        LOG.debug("Request to update MetaPermissionGroup : {}", metaPermissionGroupDTO);
        MetaPermissionGroup metaPermissionGroup = metaPermissionGroupMapper.toEntity(metaPermissionGroupDTO);
        metaPermissionGroup = metaPermissionGroupRepository.save(metaPermissionGroup);
        metaPermissionGroupSearchRepository.index(metaPermissionGroup);
        return metaPermissionGroupMapper.toDto(metaPermissionGroup);
    }

    /**
     * Partially update a metaPermissionGroup.
     *
     * @param metaPermissionGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaPermissionGroupDTO> partialUpdate(MetaPermissionGroupDTO metaPermissionGroupDTO) {
        LOG.debug("Request to partially update MetaPermissionGroup : {}", metaPermissionGroupDTO);

        return metaPermissionGroupRepository
            .findById(metaPermissionGroupDTO.getId())
            .map(existingMetaPermissionGroup -> {
                metaPermissionGroupMapper.partialUpdate(existingMetaPermissionGroup, metaPermissionGroupDTO);

                return existingMetaPermissionGroup;
            })
            .map(metaPermissionGroupRepository::save)
            .map(savedMetaPermissionGroup -> {
                metaPermissionGroupSearchRepository.index(savedMetaPermissionGroup);
                return savedMetaPermissionGroup;
            })
            .map(metaPermissionGroupMapper::toDto);
    }

    /**
     * Get one metaPermissionGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaPermissionGroupDTO> findOne(Long id) {
        LOG.debug("Request to get MetaPermissionGroup : {}", id);
        return metaPermissionGroupRepository.findById(id).map(metaPermissionGroupMapper::toDto);
    }

    /**
     * Delete the metaPermissionGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaPermissionGroup : {}", id);
        metaPermissionGroupRepository.deleteById(id);
        metaPermissionGroupSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaPermissionGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaPermissionGroupDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaPermissionGroups for query {}", query);
        return metaPermissionGroupSearchRepository.search(query, pageable).map(metaPermissionGroupMapper::toDto);
    }
}
