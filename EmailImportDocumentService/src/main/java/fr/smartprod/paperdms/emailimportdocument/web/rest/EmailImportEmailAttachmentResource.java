package fr.smartprod.paperdms.emailimportdocument.web.rest;

import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportEmailAttachmentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportEmailAttachmentQueryService;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportEmailAttachmentService;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportEmailAttachmentCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportEmailAttachmentDTO;
import fr.smartprod.paperdms.emailimportdocument.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment}.
 */
@RestController
@RequestMapping("/api/email-import-email-attachments")
public class EmailImportEmailAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportEmailAttachmentResource.class);

    private static final String ENTITY_NAME = "emailImportDocumentServiceEmailImportEmailAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailImportEmailAttachmentService emailImportEmailAttachmentService;

    private final EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository;

    private final EmailImportEmailAttachmentQueryService emailImportEmailAttachmentQueryService;

    public EmailImportEmailAttachmentResource(
        EmailImportEmailAttachmentService emailImportEmailAttachmentService,
        EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository,
        EmailImportEmailAttachmentQueryService emailImportEmailAttachmentQueryService
    ) {
        this.emailImportEmailAttachmentService = emailImportEmailAttachmentService;
        this.emailImportEmailAttachmentRepository = emailImportEmailAttachmentRepository;
        this.emailImportEmailAttachmentQueryService = emailImportEmailAttachmentQueryService;
    }

    /**
     * {@code POST  /email-import-email-attachments} : Create a new emailImportEmailAttachment.
     *
     * @param emailImportEmailAttachmentDTO the emailImportEmailAttachmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailImportEmailAttachmentDTO, or with status {@code 400 (Bad Request)} if the emailImportEmailAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailImportEmailAttachmentDTO> createEmailImportEmailAttachment(
        @Valid @RequestBody EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmailImportEmailAttachment : {}", emailImportEmailAttachmentDTO);
        if (emailImportEmailAttachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailImportEmailAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailImportEmailAttachmentDTO = emailImportEmailAttachmentService.save(emailImportEmailAttachmentDTO);
        return ResponseEntity.created(new URI("/api/email-import-email-attachments/" + emailImportEmailAttachmentDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailImportEmailAttachmentDTO.getId().toString())
            )
            .body(emailImportEmailAttachmentDTO);
    }

    /**
     * {@code PUT  /email-import-email-attachments/:id} : Updates an existing emailImportEmailAttachment.
     *
     * @param id the id of the emailImportEmailAttachmentDTO to save.
     * @param emailImportEmailAttachmentDTO the emailImportEmailAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportEmailAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportEmailAttachmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailImportEmailAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailImportEmailAttachmentDTO> updateEmailImportEmailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailImportEmailAttachment : {}, {}", id, emailImportEmailAttachmentDTO);
        if (emailImportEmailAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportEmailAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportEmailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailImportEmailAttachmentDTO = emailImportEmailAttachmentService.update(emailImportEmailAttachmentDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportEmailAttachmentDTO.getId().toString())
            )
            .body(emailImportEmailAttachmentDTO);
    }

    /**
     * {@code PATCH  /email-import-email-attachments/:id} : Partial updates given fields of an existing emailImportEmailAttachment, field will ignore if it is null
     *
     * @param id the id of the emailImportEmailAttachmentDTO to save.
     * @param emailImportEmailAttachmentDTO the emailImportEmailAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportEmailAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportEmailAttachmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailImportEmailAttachmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailImportEmailAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailImportEmailAttachmentDTO> partialUpdateEmailImportEmailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailImportEmailAttachment partially : {}, {}", id, emailImportEmailAttachmentDTO);
        if (emailImportEmailAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportEmailAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportEmailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailImportEmailAttachmentDTO> result = emailImportEmailAttachmentService.partialUpdate(emailImportEmailAttachmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportEmailAttachmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-import-email-attachments} : get all the emailImportEmailAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailImportEmailAttachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailImportEmailAttachmentDTO>> getAllEmailImportEmailAttachments(
        EmailImportEmailAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailImportEmailAttachments by criteria: {}", criteria);

        Page<EmailImportEmailAttachmentDTO> page = emailImportEmailAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-import-email-attachments/count} : count all the emailImportEmailAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailImportEmailAttachments(EmailImportEmailAttachmentCriteria criteria) {
        LOG.debug("REST request to count EmailImportEmailAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailImportEmailAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-import-email-attachments/:id} : get the "id" emailImportEmailAttachment.
     *
     * @param id the id of the emailImportEmailAttachmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailImportEmailAttachmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailImportEmailAttachmentDTO> getEmailImportEmailAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailImportEmailAttachment : {}", id);
        Optional<EmailImportEmailAttachmentDTO> emailImportEmailAttachmentDTO = emailImportEmailAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailImportEmailAttachmentDTO);
    }

    /**
     * {@code DELETE  /email-import-email-attachments/:id} : delete the "id" emailImportEmailAttachment.
     *
     * @param id the id of the emailImportEmailAttachmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailImportEmailAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailImportEmailAttachment : {}", id);
        emailImportEmailAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
