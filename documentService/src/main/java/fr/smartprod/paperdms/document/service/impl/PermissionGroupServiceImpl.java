package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.PermissionGroup;
import fr.smartprod.paperdms.document.repository.PermissionGroupRepository;
import fr.smartprod.paperdms.document.service.PermissionGroupService;
import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import fr.smartprod.paperdms.document.service.mapper.PermissionGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.PermissionGroup}.
 */
@Service
@Transactional
public class PermissionGroupServiceImpl implements PermissionGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionGroupServiceImpl.class);

    private final PermissionGroupRepository permissionGroupRepository;

    private final PermissionGroupMapper permissionGroupMapper;

    public PermissionGroupServiceImpl(PermissionGroupRepository permissionGroupRepository, PermissionGroupMapper permissionGroupMapper) {
        this.permissionGroupRepository = permissionGroupRepository;
        this.permissionGroupMapper = permissionGroupMapper;
    }

    @Override
    public PermissionGroupDTO save(PermissionGroupDTO permissionGroupDTO) {
        LOG.debug("Request to save PermissionGroup : {}", permissionGroupDTO);
        PermissionGroup permissionGroup = permissionGroupMapper.toEntity(permissionGroupDTO);
        permissionGroup = permissionGroupRepository.save(permissionGroup);
        return permissionGroupMapper.toDto(permissionGroup);
    }

    @Override
    public PermissionGroupDTO update(PermissionGroupDTO permissionGroupDTO) {
        LOG.debug("Request to update PermissionGroup : {}", permissionGroupDTO);
        PermissionGroup permissionGroup = permissionGroupMapper.toEntity(permissionGroupDTO);
        permissionGroup = permissionGroupRepository.save(permissionGroup);
        return permissionGroupMapper.toDto(permissionGroup);
    }

    @Override
    public Optional<PermissionGroupDTO> partialUpdate(PermissionGroupDTO permissionGroupDTO) {
        LOG.debug("Request to partially update PermissionGroup : {}", permissionGroupDTO);

        return permissionGroupRepository
            .findById(permissionGroupDTO.getId())
            .map(existingPermissionGroup -> {
                permissionGroupMapper.partialUpdate(existingPermissionGroup, permissionGroupDTO);

                return existingPermissionGroup;
            })
            .map(permissionGroupRepository::save)
            .map(permissionGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionGroupDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PermissionGroups");
        return permissionGroupRepository.findAll(pageable).map(permissionGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionGroupDTO> findOne(Long id) {
        LOG.debug("Request to get PermissionGroup : {}", id);
        return permissionGroupRepository.findById(id).map(permissionGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PermissionGroup : {}", id);
        permissionGroupRepository.deleteById(id);
    }
}
