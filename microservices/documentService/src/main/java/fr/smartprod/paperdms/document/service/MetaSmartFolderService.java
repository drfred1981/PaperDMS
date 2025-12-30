package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import fr.smartprod.paperdms.document.repository.MetaSmartFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSmartFolderSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaSmartFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSmartFolderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaSmartFolder}.
 */
@Service
@Transactional
public class MetaSmartFolderService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSmartFolderService.class);

    private final MetaSmartFolderRepository metaSmartFolderRepository;

    private final MetaSmartFolderMapper metaSmartFolderMapper;

    private final MetaSmartFolderSearchRepository metaSmartFolderSearchRepository;

    public MetaSmartFolderService(
        MetaSmartFolderRepository metaSmartFolderRepository,
        MetaSmartFolderMapper metaSmartFolderMapper,
        MetaSmartFolderSearchRepository metaSmartFolderSearchRepository
    ) {
        this.metaSmartFolderRepository = metaSmartFolderRepository;
        this.metaSmartFolderMapper = metaSmartFolderMapper;
        this.metaSmartFolderSearchRepository = metaSmartFolderSearchRepository;
    }

    /**
     * Save a metaSmartFolder.
     *
     * @param metaSmartFolderDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaSmartFolderDTO save(MetaSmartFolderDTO metaSmartFolderDTO) {
        LOG.debug("Request to save MetaSmartFolder : {}", metaSmartFolderDTO);
        MetaSmartFolder metaSmartFolder = metaSmartFolderMapper.toEntity(metaSmartFolderDTO);
        metaSmartFolder = metaSmartFolderRepository.save(metaSmartFolder);
        metaSmartFolderSearchRepository.index(metaSmartFolder);
        return metaSmartFolderMapper.toDto(metaSmartFolder);
    }

    /**
     * Update a metaSmartFolder.
     *
     * @param metaSmartFolderDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaSmartFolderDTO update(MetaSmartFolderDTO metaSmartFolderDTO) {
        LOG.debug("Request to update MetaSmartFolder : {}", metaSmartFolderDTO);
        MetaSmartFolder metaSmartFolder = metaSmartFolderMapper.toEntity(metaSmartFolderDTO);
        metaSmartFolder = metaSmartFolderRepository.save(metaSmartFolder);
        metaSmartFolderSearchRepository.index(metaSmartFolder);
        return metaSmartFolderMapper.toDto(metaSmartFolder);
    }

    /**
     * Partially update a metaSmartFolder.
     *
     * @param metaSmartFolderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaSmartFolderDTO> partialUpdate(MetaSmartFolderDTO metaSmartFolderDTO) {
        LOG.debug("Request to partially update MetaSmartFolder : {}", metaSmartFolderDTO);

        return metaSmartFolderRepository
            .findById(metaSmartFolderDTO.getId())
            .map(existingMetaSmartFolder -> {
                metaSmartFolderMapper.partialUpdate(existingMetaSmartFolder, metaSmartFolderDTO);

                return existingMetaSmartFolder;
            })
            .map(metaSmartFolderRepository::save)
            .map(savedMetaSmartFolder -> {
                metaSmartFolderSearchRepository.index(savedMetaSmartFolder);
                return savedMetaSmartFolder;
            })
            .map(metaSmartFolderMapper::toDto);
    }

    /**
     * Get one metaSmartFolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaSmartFolderDTO> findOne(Long id) {
        LOG.debug("Request to get MetaSmartFolder : {}", id);
        return metaSmartFolderRepository.findById(id).map(metaSmartFolderMapper::toDto);
    }

    /**
     * Delete the metaSmartFolder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaSmartFolder : {}", id);
        metaSmartFolderRepository.deleteById(id);
        metaSmartFolderSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaSmartFolder corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaSmartFolderDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaSmartFolders for query {}", query);
        return metaSmartFolderSearchRepository.search(query, pageable).map(metaSmartFolderMapper::toDto);
    }
}
