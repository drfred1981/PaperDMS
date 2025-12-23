package fr.smartprod.paperdms.scan.service.impl;

import fr.smartprod.paperdms.scan.domain.ScannedPage;
import fr.smartprod.paperdms.scan.repository.ScannedPageRepository;
import fr.smartprod.paperdms.scan.service.ScannedPageService;
import fr.smartprod.paperdms.scan.service.dto.ScannedPageDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScannedPageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.scan.domain.ScannedPage}.
 */
@Service
@Transactional
public class ScannedPageServiceImpl implements ScannedPageService {

    private static final Logger LOG = LoggerFactory.getLogger(ScannedPageServiceImpl.class);

    private final ScannedPageRepository scannedPageRepository;

    private final ScannedPageMapper scannedPageMapper;

    public ScannedPageServiceImpl(ScannedPageRepository scannedPageRepository, ScannedPageMapper scannedPageMapper) {
        this.scannedPageRepository = scannedPageRepository;
        this.scannedPageMapper = scannedPageMapper;
    }

    @Override
    public ScannedPageDTO save(ScannedPageDTO scannedPageDTO) {
        LOG.debug("Request to save ScannedPage : {}", scannedPageDTO);
        ScannedPage scannedPage = scannedPageMapper.toEntity(scannedPageDTO);
        scannedPage = scannedPageRepository.save(scannedPage);
        return scannedPageMapper.toDto(scannedPage);
    }

    @Override
    public ScannedPageDTO update(ScannedPageDTO scannedPageDTO) {
        LOG.debug("Request to update ScannedPage : {}", scannedPageDTO);
        ScannedPage scannedPage = scannedPageMapper.toEntity(scannedPageDTO);
        scannedPage = scannedPageRepository.save(scannedPage);
        return scannedPageMapper.toDto(scannedPage);
    }

    @Override
    public Optional<ScannedPageDTO> partialUpdate(ScannedPageDTO scannedPageDTO) {
        LOG.debug("Request to partially update ScannedPage : {}", scannedPageDTO);

        return scannedPageRepository
            .findById(scannedPageDTO.getId())
            .map(existingScannedPage -> {
                scannedPageMapper.partialUpdate(existingScannedPage, scannedPageDTO);

                return existingScannedPage;
            })
            .map(scannedPageRepository::save)
            .map(scannedPageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScannedPageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ScannedPages");
        return scannedPageRepository.findAll(pageable).map(scannedPageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScannedPageDTO> findOne(Long id) {
        LOG.debug("Request to get ScannedPage : {}", id);
        return scannedPageRepository.findById(id).map(scannedPageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScannedPage : {}", id);
        scannedPageRepository.deleteById(id);
    }
}
