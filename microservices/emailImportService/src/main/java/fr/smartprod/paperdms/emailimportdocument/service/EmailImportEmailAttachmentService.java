package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportEmailAttachmentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportEmailAttachmentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportEmailAttachmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment}.
 */
@Service
@Transactional
public class EmailImportEmailAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportEmailAttachmentService.class);

    private final EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository;

    private final EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper;

    public EmailImportEmailAttachmentService(
        EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository,
        EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper
    ) {
        this.emailImportEmailAttachmentRepository = emailImportEmailAttachmentRepository;
        this.emailImportEmailAttachmentMapper = emailImportEmailAttachmentMapper;
    }

    /**
     * Save a emailImportEmailAttachment.
     *
     * @param emailImportEmailAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportEmailAttachmentDTO save(EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO) {
        LOG.debug("Request to save EmailImportEmailAttachment : {}", emailImportEmailAttachmentDTO);
        EmailImportEmailAttachment emailImportEmailAttachment = emailImportEmailAttachmentMapper.toEntity(emailImportEmailAttachmentDTO);
        emailImportEmailAttachment = emailImportEmailAttachmentRepository.save(emailImportEmailAttachment);
        return emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);
    }

    /**
     * Update a emailImportEmailAttachment.
     *
     * @param emailImportEmailAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportEmailAttachmentDTO update(EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO) {
        LOG.debug("Request to update EmailImportEmailAttachment : {}", emailImportEmailAttachmentDTO);
        EmailImportEmailAttachment emailImportEmailAttachment = emailImportEmailAttachmentMapper.toEntity(emailImportEmailAttachmentDTO);
        emailImportEmailAttachment = emailImportEmailAttachmentRepository.save(emailImportEmailAttachment);
        return emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);
    }

    /**
     * Partially update a emailImportEmailAttachment.
     *
     * @param emailImportEmailAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailImportEmailAttachmentDTO> partialUpdate(EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO) {
        LOG.debug("Request to partially update EmailImportEmailAttachment : {}", emailImportEmailAttachmentDTO);

        return emailImportEmailAttachmentRepository
            .findById(emailImportEmailAttachmentDTO.getId())
            .map(existingEmailImportEmailAttachment -> {
                emailImportEmailAttachmentMapper.partialUpdate(existingEmailImportEmailAttachment, emailImportEmailAttachmentDTO);

                return existingEmailImportEmailAttachment;
            })
            .map(emailImportEmailAttachmentRepository::save)
            .map(emailImportEmailAttachmentMapper::toDto);
    }

    /**
     * Get one emailImportEmailAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailImportEmailAttachmentDTO> findOne(Long id) {
        LOG.debug("Request to get EmailImportEmailAttachment : {}", id);
        return emailImportEmailAttachmentRepository.findById(id).map(emailImportEmailAttachmentMapper::toDto);
    }

    /**
     * Delete the emailImportEmailAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailImportEmailAttachment : {}", id);
        emailImportEmailAttachmentRepository.deleteById(id);
    }
}
