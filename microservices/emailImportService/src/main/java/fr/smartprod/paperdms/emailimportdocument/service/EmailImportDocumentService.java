package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportDocumentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument}.
 */
@Service
@Transactional
public class EmailImportDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportDocumentService.class);

    private final EmailImportDocumentRepository emailImportDocumentRepository;

    private final EmailImportDocumentMapper emailImportDocumentMapper;

    public EmailImportDocumentService(
        EmailImportDocumentRepository emailImportDocumentRepository,
        EmailImportDocumentMapper emailImportDocumentMapper
    ) {
        this.emailImportDocumentRepository = emailImportDocumentRepository;
        this.emailImportDocumentMapper = emailImportDocumentMapper;
    }

    /**
     * Save a emailImportDocument.
     *
     * @param emailImportDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportDocumentDTO save(EmailImportDocumentDTO emailImportDocumentDTO) {
        LOG.debug("Request to save EmailImportDocument : {}", emailImportDocumentDTO);
        EmailImportDocument emailImportDocument = emailImportDocumentMapper.toEntity(emailImportDocumentDTO);
        emailImportDocument = emailImportDocumentRepository.save(emailImportDocument);
        return emailImportDocumentMapper.toDto(emailImportDocument);
    }

    /**
     * Update a emailImportDocument.
     *
     * @param emailImportDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportDocumentDTO update(EmailImportDocumentDTO emailImportDocumentDTO) {
        LOG.debug("Request to update EmailImportDocument : {}", emailImportDocumentDTO);
        EmailImportDocument emailImportDocument = emailImportDocumentMapper.toEntity(emailImportDocumentDTO);
        emailImportDocument = emailImportDocumentRepository.save(emailImportDocument);
        return emailImportDocumentMapper.toDto(emailImportDocument);
    }

    /**
     * Partially update a emailImportDocument.
     *
     * @param emailImportDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailImportDocumentDTO> partialUpdate(EmailImportDocumentDTO emailImportDocumentDTO) {
        LOG.debug("Request to partially update EmailImportDocument : {}", emailImportDocumentDTO);

        return emailImportDocumentRepository
            .findById(emailImportDocumentDTO.getId())
            .map(existingEmailImportDocument -> {
                emailImportDocumentMapper.partialUpdate(existingEmailImportDocument, emailImportDocumentDTO);

                return existingEmailImportDocument;
            })
            .map(emailImportDocumentRepository::save)
            .map(emailImportDocumentMapper::toDto);
    }

    /**
     * Get one emailImportDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailImportDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get EmailImportDocument : {}", id);
        return emailImportDocumentRepository.findById(id).map(emailImportDocumentMapper::toDto);
    }

    /**
     * Delete the emailImportDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailImportDocument : {}", id);
        emailImportDocumentRepository.deleteById(id);
    }
}
