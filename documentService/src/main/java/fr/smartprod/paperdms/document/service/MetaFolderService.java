package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.repository.MetaFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaFolderSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaFolderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaFolder}.
 */
@Service
@Transactional
public class MetaFolderService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaFolderService.class);

    private final MetaFolderRepository metaFolderRepository;

    private final MetaFolderMapper metaFolderMapper;

    private final MetaFolderSearchRepository metaFolderSearchRepository;

    public MetaFolderService(
        MetaFolderRepository metaFolderRepository,
        MetaFolderMapper metaFolderMapper,
        MetaFolderSearchRepository metaFolderSearchRepository
    ) {
        this.metaFolderRepository = metaFolderRepository;
        this.metaFolderMapper = metaFolderMapper;
        this.metaFolderSearchRepository = metaFolderSearchRepository;
    }

    /**
     * Save a metaFolder.
     *
     * @param metaFolderDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaFolderDTO save(MetaFolderDTO metaFolderDTO) {
        LOG.debug("Request to save MetaFolder : {}", metaFolderDTO);
        MetaFolder metaFolder = metaFolderMapper.toEntity(metaFolderDTO);
        metaFolder = metaFolderRepository.save(metaFolder);
        metaFolderSearchRepository.index(metaFolder);
        return metaFolderMapper.toDto(metaFolder);
    }

    /**
     * Update a metaFolder.
     *
     * @param metaFolderDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaFolderDTO update(MetaFolderDTO metaFolderDTO) {
        LOG.debug("Request to update MetaFolder : {}", metaFolderDTO);
        MetaFolder metaFolder = metaFolderMapper.toEntity(metaFolderDTO);
        metaFolder = metaFolderRepository.save(metaFolder);
        metaFolderSearchRepository.index(metaFolder);
        return metaFolderMapper.toDto(metaFolder);
    }

    /**
     * Partially update a metaFolder.
     *
     * @param metaFolderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaFolderDTO> partialUpdate(MetaFolderDTO metaFolderDTO) {
        LOG.debug("Request to partially update MetaFolder : {}", metaFolderDTO);

        return metaFolderRepository
            .findById(metaFolderDTO.getId())
            .map(existingMetaFolder -> {
                metaFolderMapper.partialUpdate(existingMetaFolder, metaFolderDTO);

                return existingMetaFolder;
            })
            .map(metaFolderRepository::save)
            .map(savedMetaFolder -> {
                metaFolderSearchRepository.index(savedMetaFolder);
                return savedMetaFolder;
            })
            .map(metaFolderMapper::toDto);
    }

    /**
     * Get one metaFolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaFolderDTO> findOne(Long id) {
        LOG.debug("Request to get MetaFolder : {}", id);
        return metaFolderRepository.findById(id).map(metaFolderMapper::toDto);
    }

    /**
     * Delete the metaFolder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaFolder : {}", id);
        metaFolderRepository.deleteById(id);
        metaFolderSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaFolder corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaFolderDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaFolders for query {}", query);
        return metaFolderSearchRepository.search(query, pageable).map(metaFolderMapper::toDto);
    }
}
