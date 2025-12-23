package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.repository.ManualRepository;
import fr.smartprod.paperdms.business.repository.search.ManualSearchRepository;
import fr.smartprod.paperdms.business.service.ManualService;
import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import fr.smartprod.paperdms.business.service.mapper.ManualMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.Manual}.
 */
@Service
@Transactional
public class ManualServiceImpl implements ManualService {

    private static final Logger LOG = LoggerFactory.getLogger(ManualServiceImpl.class);

    private final ManualRepository manualRepository;

    private final ManualMapper manualMapper;

    private final ManualSearchRepository manualSearchRepository;

    public ManualServiceImpl(ManualRepository manualRepository, ManualMapper manualMapper, ManualSearchRepository manualSearchRepository) {
        this.manualRepository = manualRepository;
        this.manualMapper = manualMapper;
        this.manualSearchRepository = manualSearchRepository;
    }

    @Override
    public ManualDTO save(ManualDTO manualDTO) {
        LOG.debug("Request to save Manual : {}", manualDTO);
        Manual manual = manualMapper.toEntity(manualDTO);
        manual = manualRepository.save(manual);
        manualSearchRepository.index(manual);
        return manualMapper.toDto(manual);
    }

    @Override
    public ManualDTO update(ManualDTO manualDTO) {
        LOG.debug("Request to update Manual : {}", manualDTO);
        Manual manual = manualMapper.toEntity(manualDTO);
        manual = manualRepository.save(manual);
        manualSearchRepository.index(manual);
        return manualMapper.toDto(manual);
    }

    @Override
    public Optional<ManualDTO> partialUpdate(ManualDTO manualDTO) {
        LOG.debug("Request to partially update Manual : {}", manualDTO);

        return manualRepository
            .findById(manualDTO.getId())
            .map(existingManual -> {
                manualMapper.partialUpdate(existingManual, manualDTO);

                return existingManual;
            })
            .map(manualRepository::save)
            .map(savedManual -> {
                manualSearchRepository.index(savedManual);
                return savedManual;
            })
            .map(manualMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManualDTO> findOne(Long id) {
        LOG.debug("Request to get Manual : {}", id);
        return manualRepository.findById(id).map(manualMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Manual : {}", id);
        manualRepository.deleteById(id);
        manualSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManualDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Manuals for query {}", query);
        return manualSearchRepository.search(query, pageable).map(manualMapper::toDto);
    }
}
