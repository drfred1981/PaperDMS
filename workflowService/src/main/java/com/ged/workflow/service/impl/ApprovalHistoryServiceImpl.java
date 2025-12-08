package com.ged.workflow.service.impl;

import com.ged.workflow.domain.ApprovalHistory;
import com.ged.workflow.repository.ApprovalHistoryRepository;
import com.ged.workflow.service.ApprovalHistoryService;
import com.ged.workflow.service.dto.ApprovalHistoryDTO;
import com.ged.workflow.service.mapper.ApprovalHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.workflow.domain.ApprovalHistory}.
 */
@Service
@Transactional
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ApprovalHistoryServiceImpl.class);

    private final ApprovalHistoryRepository approvalHistoryRepository;

    private final ApprovalHistoryMapper approvalHistoryMapper;

    public ApprovalHistoryServiceImpl(ApprovalHistoryRepository approvalHistoryRepository, ApprovalHistoryMapper approvalHistoryMapper) {
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.approvalHistoryMapper = approvalHistoryMapper;
    }

    @Override
    public ApprovalHistoryDTO save(ApprovalHistoryDTO approvalHistoryDTO) {
        LOG.debug("Request to save ApprovalHistory : {}", approvalHistoryDTO);
        ApprovalHistory approvalHistory = approvalHistoryMapper.toEntity(approvalHistoryDTO);
        approvalHistory = approvalHistoryRepository.save(approvalHistory);
        return approvalHistoryMapper.toDto(approvalHistory);
    }

    @Override
    public ApprovalHistoryDTO update(ApprovalHistoryDTO approvalHistoryDTO) {
        LOG.debug("Request to update ApprovalHistory : {}", approvalHistoryDTO);
        ApprovalHistory approvalHistory = approvalHistoryMapper.toEntity(approvalHistoryDTO);
        approvalHistory = approvalHistoryRepository.save(approvalHistory);
        return approvalHistoryMapper.toDto(approvalHistory);
    }

    @Override
    public Optional<ApprovalHistoryDTO> partialUpdate(ApprovalHistoryDTO approvalHistoryDTO) {
        LOG.debug("Request to partially update ApprovalHistory : {}", approvalHistoryDTO);

        return approvalHistoryRepository
            .findById(approvalHistoryDTO.getId())
            .map(existingApprovalHistory -> {
                approvalHistoryMapper.partialUpdate(existingApprovalHistory, approvalHistoryDTO);

                return existingApprovalHistory;
            })
            .map(approvalHistoryRepository::save)
            .map(approvalHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ApprovalHistories");
        return approvalHistoryRepository.findAll(pageable).map(approvalHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get ApprovalHistory : {}", id);
        return approvalHistoryRepository.findById(id).map(approvalHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ApprovalHistory : {}", id);
        approvalHistoryRepository.deleteById(id);
    }
}
