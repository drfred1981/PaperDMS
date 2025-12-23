package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.SavedSearch;
import fr.smartprod.paperdms.document.repository.SavedSearchRepository;
import fr.smartprod.paperdms.document.service.SavedSearchService;
import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.SavedSearchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.SavedSearch}.
 */
@Service
@Transactional
public class SavedSearchServiceImpl implements SavedSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(SavedSearchServiceImpl.class);

    private final SavedSearchRepository savedSearchRepository;

    private final SavedSearchMapper savedSearchMapper;

    public SavedSearchServiceImpl(SavedSearchRepository savedSearchRepository, SavedSearchMapper savedSearchMapper) {
        this.savedSearchRepository = savedSearchRepository;
        this.savedSearchMapper = savedSearchMapper;
    }

    @Override
    public SavedSearchDTO save(SavedSearchDTO savedSearchDTO) {
        LOG.debug("Request to save SavedSearch : {}", savedSearchDTO);
        SavedSearch savedSearch = savedSearchMapper.toEntity(savedSearchDTO);
        savedSearch = savedSearchRepository.save(savedSearch);
        return savedSearchMapper.toDto(savedSearch);
    }

    @Override
    public SavedSearchDTO update(SavedSearchDTO savedSearchDTO) {
        LOG.debug("Request to update SavedSearch : {}", savedSearchDTO);
        SavedSearch savedSearch = savedSearchMapper.toEntity(savedSearchDTO);
        savedSearch = savedSearchRepository.save(savedSearch);
        return savedSearchMapper.toDto(savedSearch);
    }

    @Override
    public Optional<SavedSearchDTO> partialUpdate(SavedSearchDTO savedSearchDTO) {
        LOG.debug("Request to partially update SavedSearch : {}", savedSearchDTO);

        return savedSearchRepository
            .findById(savedSearchDTO.getId())
            .map(existingSavedSearch -> {
                savedSearchMapper.partialUpdate(existingSavedSearch, savedSearchDTO);

                return existingSavedSearch;
            })
            .map(savedSearchRepository::save)
            .map(savedSearchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SavedSearchDTO> findOne(Long id) {
        LOG.debug("Request to get SavedSearch : {}", id);
        return savedSearchRepository.findById(id).map(savedSearchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SavedSearch : {}", id);
        savedSearchRepository.deleteById(id);
    }
}
