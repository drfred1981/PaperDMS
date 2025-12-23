package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.ManualChapter;
import fr.smartprod.paperdms.business.repository.ManualChapterRepository;
import fr.smartprod.paperdms.business.service.ManualChapterService;
import fr.smartprod.paperdms.business.service.dto.ManualChapterDTO;
import fr.smartprod.paperdms.business.service.mapper.ManualChapterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.ManualChapter}.
 */
@Service
@Transactional
public class ManualChapterServiceImpl implements ManualChapterService {

    private static final Logger LOG = LoggerFactory.getLogger(ManualChapterServiceImpl.class);

    private final ManualChapterRepository manualChapterRepository;

    private final ManualChapterMapper manualChapterMapper;

    public ManualChapterServiceImpl(ManualChapterRepository manualChapterRepository, ManualChapterMapper manualChapterMapper) {
        this.manualChapterRepository = manualChapterRepository;
        this.manualChapterMapper = manualChapterMapper;
    }

    @Override
    public ManualChapterDTO save(ManualChapterDTO manualChapterDTO) {
        LOG.debug("Request to save ManualChapter : {}", manualChapterDTO);
        ManualChapter manualChapter = manualChapterMapper.toEntity(manualChapterDTO);
        manualChapter = manualChapterRepository.save(manualChapter);
        return manualChapterMapper.toDto(manualChapter);
    }

    @Override
    public ManualChapterDTO update(ManualChapterDTO manualChapterDTO) {
        LOG.debug("Request to update ManualChapter : {}", manualChapterDTO);
        ManualChapter manualChapter = manualChapterMapper.toEntity(manualChapterDTO);
        manualChapter = manualChapterRepository.save(manualChapter);
        return manualChapterMapper.toDto(manualChapter);
    }

    @Override
    public Optional<ManualChapterDTO> partialUpdate(ManualChapterDTO manualChapterDTO) {
        LOG.debug("Request to partially update ManualChapter : {}", manualChapterDTO);

        return manualChapterRepository
            .findById(manualChapterDTO.getId())
            .map(existingManualChapter -> {
                manualChapterMapper.partialUpdate(existingManualChapter, manualChapterDTO);

                return existingManualChapter;
            })
            .map(manualChapterRepository::save)
            .map(manualChapterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManualChapterDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ManualChapters");
        return manualChapterRepository.findAll(pageable).map(manualChapterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManualChapterDTO> findOne(Long id) {
        LOG.debug("Request to get ManualChapter : {}", id);
        return manualChapterRepository.findById(id).map(manualChapterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ManualChapter : {}", id);
        manualChapterRepository.deleteById(id);
    }
}
