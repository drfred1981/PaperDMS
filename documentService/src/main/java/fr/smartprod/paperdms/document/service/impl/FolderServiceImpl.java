package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.repository.FolderRepository;
import fr.smartprod.paperdms.document.service.FolderService;
import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import fr.smartprod.paperdms.document.service.mapper.FolderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.Folder}.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FolderServiceImpl.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    public FolderServiceImpl(FolderRepository folderRepository, FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    @Override
    public FolderDTO save(FolderDTO folderDTO) {
        LOG.debug("Request to save Folder : {}", folderDTO);
        Folder folder = folderMapper.toEntity(folderDTO);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public FolderDTO update(FolderDTO folderDTO) {
        LOG.debug("Request to update Folder : {}", folderDTO);
        Folder folder = folderMapper.toEntity(folderDTO);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public Optional<FolderDTO> partialUpdate(FolderDTO folderDTO) {
        LOG.debug("Request to partially update Folder : {}", folderDTO);

        return folderRepository
            .findById(folderDTO.getId())
            .map(existingFolder -> {
                folderMapper.partialUpdate(existingFolder, folderDTO);

                return existingFolder;
            })
            .map(folderRepository::save)
            .map(folderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FolderDTO> findOne(Long id) {
        LOG.debug("Request to get Folder : {}", id);
        return folderRepository.findById(id).map(folderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Folder : {}", id);
        folderRepository.deleteById(id);
    }
}
