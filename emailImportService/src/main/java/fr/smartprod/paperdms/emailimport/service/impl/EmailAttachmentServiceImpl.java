package fr.smartprod.paperdms.emailimport.service.impl;

import fr.smartprod.paperdms.emailimport.domain.EmailAttachment;
import fr.smartprod.paperdms.emailimport.repository.EmailAttachmentRepository;
import fr.smartprod.paperdms.emailimport.service.EmailAttachmentService;
import fr.smartprod.paperdms.emailimport.service.dto.EmailAttachmentDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.EmailAttachmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailAttachment}.
 */
@Service
@Transactional
public class EmailAttachmentServiceImpl implements EmailAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailAttachmentServiceImpl.class);

    private final EmailAttachmentRepository emailAttachmentRepository;

    private final EmailAttachmentMapper emailAttachmentMapper;

    public EmailAttachmentServiceImpl(EmailAttachmentRepository emailAttachmentRepository, EmailAttachmentMapper emailAttachmentMapper) {
        this.emailAttachmentRepository = emailAttachmentRepository;
        this.emailAttachmentMapper = emailAttachmentMapper;
    }

    @Override
    public EmailAttachmentDTO save(EmailAttachmentDTO emailAttachmentDTO) {
        LOG.debug("Request to save EmailAttachment : {}", emailAttachmentDTO);
        EmailAttachment emailAttachment = emailAttachmentMapper.toEntity(emailAttachmentDTO);
        emailAttachment = emailAttachmentRepository.save(emailAttachment);
        return emailAttachmentMapper.toDto(emailAttachment);
    }

    @Override
    public EmailAttachmentDTO update(EmailAttachmentDTO emailAttachmentDTO) {
        LOG.debug("Request to update EmailAttachment : {}", emailAttachmentDTO);
        EmailAttachment emailAttachment = emailAttachmentMapper.toEntity(emailAttachmentDTO);
        emailAttachment = emailAttachmentRepository.save(emailAttachment);
        return emailAttachmentMapper.toDto(emailAttachment);
    }

    @Override
    public Optional<EmailAttachmentDTO> partialUpdate(EmailAttachmentDTO emailAttachmentDTO) {
        LOG.debug("Request to partially update EmailAttachment : {}", emailAttachmentDTO);

        return emailAttachmentRepository
            .findById(emailAttachmentDTO.getId())
            .map(existingEmailAttachment -> {
                emailAttachmentMapper.partialUpdate(existingEmailAttachment, emailAttachmentDTO);

                return existingEmailAttachment;
            })
            .map(emailAttachmentRepository::save)
            .map(emailAttachmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailAttachmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all EmailAttachments");
        return emailAttachmentRepository.findAll(pageable).map(emailAttachmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailAttachmentDTO> findOne(Long id) {
        LOG.debug("Request to get EmailAttachment : {}", id);
        return emailAttachmentRepository.findById(id).map(emailAttachmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmailAttachment : {}", id);
        emailAttachmentRepository.deleteById(id);
    }
}
