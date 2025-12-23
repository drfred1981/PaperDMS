package fr.smartprod.paperdms.emailimport.web.rest;

import fr.smartprod.paperdms.emailimport.repository.EmailAttachmentRepository;
import fr.smartprod.paperdms.emailimport.service.EmailAttachmentService;
import fr.smartprod.paperdms.emailimport.service.dto.EmailAttachmentDTO;
import fr.smartprod.paperdms.emailimport.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailAttachment}.
 */
@RestController
@RequestMapping("/api/email-attachments")
public class EmailAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailAttachmentResource.class);

    private static final String ENTITY_NAME = "emailImportServiceEmailAttachment";

    @Value("${jhipster.clientApp.name:emailImportService}")
    private String applicationName;

    private final EmailAttachmentService emailAttachmentService;

    private final EmailAttachmentRepository emailAttachmentRepository;

    public EmailAttachmentResource(EmailAttachmentService emailAttachmentService, EmailAttachmentRepository emailAttachmentRepository) {
        this.emailAttachmentService = emailAttachmentService;
        this.emailAttachmentRepository = emailAttachmentRepository;
    }

    /**
     * {@code POST  /email-attachments} : Create a new emailAttachment.
     *
     * @param emailAttachmentDTO the emailAttachmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailAttachmentDTO, or with status {@code 400 (Bad Request)} if the emailAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailAttachmentDTO> createEmailAttachment(@Valid @RequestBody EmailAttachmentDTO emailAttachmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmailAttachment : {}", emailAttachmentDTO);
        if (emailAttachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailAttachmentDTO = emailAttachmentService.save(emailAttachmentDTO);
        return ResponseEntity.created(new URI("/api/email-attachments/" + emailAttachmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailAttachmentDTO.getId().toString()))
            .body(emailAttachmentDTO);
    }

    /**
     * {@code PUT  /email-attachments/:id} : Updates an existing emailAttachment.
     *
     * @param id the id of the emailAttachmentDTO to save.
     * @param emailAttachmentDTO the emailAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the emailAttachmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailAttachmentDTO> updateEmailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailAttachmentDTO emailAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailAttachment : {}, {}", id, emailAttachmentDTO);
        if (emailAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailAttachmentDTO = emailAttachmentService.update(emailAttachmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailAttachmentDTO.getId().toString()))
            .body(emailAttachmentDTO);
    }

    /**
     * {@code PATCH  /email-attachments/:id} : Partial updates given fields of an existing emailAttachment, field will ignore if it is null
     *
     * @param id the id of the emailAttachmentDTO to save.
     * @param emailAttachmentDTO the emailAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the emailAttachmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailAttachmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailAttachmentDTO> partialUpdateEmailAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailAttachmentDTO emailAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailAttachment partially : {}, {}", id, emailAttachmentDTO);
        if (emailAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailAttachmentDTO> result = emailAttachmentService.partialUpdate(emailAttachmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailAttachmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-attachments} : get all the emailAttachments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailAttachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailAttachmentDTO>> getAllEmailAttachments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of EmailAttachments");
        Page<EmailAttachmentDTO> page = emailAttachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-attachments/:id} : get the "id" emailAttachment.
     *
     * @param id the id of the emailAttachmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailAttachmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailAttachmentDTO> getEmailAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailAttachment : {}", id);
        Optional<EmailAttachmentDTO> emailAttachmentDTO = emailAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailAttachmentDTO);
    }

    /**
     * {@code DELETE  /email-attachments/:id} : delete the "id" emailAttachment.
     *
     * @param id the id of the emailAttachmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailAttachment : {}", id);
        emailAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
