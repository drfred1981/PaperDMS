package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.SmartFolder;
import fr.smartprod.paperdms.document.repository.SmartFolderRepository;
import fr.smartprod.paperdms.document.service.SmartFolderService;
import fr.smartprod.paperdms.document.service.dto.SmartFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.SmartFolderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.SmartFolder}.
 */
@Service
@Transactional
public class SmartFolderServiceImpl implements SmartFolderService {

    private static final Logger LOG = LoggerFactory.getLogger(SmartFolderServiceImpl.class);

    private final SmartFolderRepository smartFolderRepository;

    private final SmartFolderMapper smartFolderMapper;

    public SmartFolderServiceImpl(SmartFolderRepository smartFolderRepository, SmartFolderMapper smartFolderMapper) {
        this.smartFolderRepository = smartFolderRepository;
        this.smartFolderMapper = smartFolderMapper;
    }

    @Override
    public SmartFolderDTO save(SmartFolderDTO smartFolderDTO) {
        LOG.debug("Request to save SmartFolder : {}", smartFolderDTO);
        SmartFolder smartFolder = smartFolderMapper.toEntity(smartFolderDTO);
        smartFolder = smartFolderRepository.save(smartFolder);
        return smartFolderMapper.toDto(smartFolder);
    }

    @Override
    public SmartFolderDTO update(SmartFolderDTO smartFolderDTO) {
        LOG.debug("Request to update SmartFolder : {}", smartFolderDTO);
        SmartFolder smartFolder = smartFolderMapper.toEntity(smartFolderDTO);
        smartFolder = smartFolderRepository.save(smartFolder);
        return smartFolderMapper.toDto(smartFolder);
    }

    @Override
    public Optional<SmartFolderDTO> partialUpdate(SmartFolderDTO smartFolderDTO) {
        LOG.debug("Request to partially update SmartFolder : {}", smartFolderDTO);

        return smartFolderRepository
            .findById(smartFolderDTO.getId())
            .map(existingSmartFolder -> {
                smartFolderMapper.partialUpdate(existingSmartFolder, smartFolderDTO);

                return existingSmartFolder;
            })
            .map(smartFolderRepository::save)
            .map(smartFolderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SmartFolderDTO> findOne(Long id) {
        LOG.debug("Request to get SmartFolder : {}", id);
        return smartFolderRepository.findById(id).map(smartFolderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SmartFolder : {}", id);
        smartFolderRepository.deleteById(id);
    }
}
