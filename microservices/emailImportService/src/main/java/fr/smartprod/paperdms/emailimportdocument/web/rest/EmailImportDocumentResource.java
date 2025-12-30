package fr.smartprod.paperdms.emailimportdocument.web.rest;

import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportDocumentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportDocumentQueryService;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportDocumentService;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportDocumentCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument}.
 */
@RestController
@RequestMapping("/api/email-import-documents")
public class EmailImportDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportDocumentResource.class);

    private static final String ENTITY_NAME = "emailImportDocumentServiceEmailImportDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailImportDocumentService emailImportDocumentService;

    private final EmailImportDocumentRepository emailImportDocumentRepository;

    private final EmailImportDocumentQueryService emailImportDocumentQueryService;

    public EmailImportDocumentResource(
        EmailImportDocumentService emailImportDocumentService,
        EmailImportDocumentRepository emailImportDocumentRepository,
        EmailImportDocumentQueryService emailImportDocumentQueryService
    ) {
        this.emailImportDocumentService = emailImportDocumentService;
        this.emailImportDocumentRepository = emailImportDocumentRepository;
        this.emailImportDocumentQueryService = emailImportDocumentQueryService;
    }

    /**
     * {@code POST  /email-import-documents} : Create a new emailImportDocument.
     *
     * @param emailImportDocumentDTO the emailImportDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailImportDocumentDTO, or with status {@code 400 (Bad Request)} if the emailImportDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailImportDocumentDTO> createEmailImportDocument(
        @Valid @RequestBody EmailImportDocumentDTO emailImportDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmailImportDocument : {}", emailImportDocumentDTO);
        if (emailImportDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailImportDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailImportDocumentDTO = emailImportDocumentService.save(emailImportDocumentDTO);
        return ResponseEntity.created(new URI("/api/email-import-documents/" + emailImportDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailImportDocumentDTO.getId().toString()))
            .body(emailImportDocumentDTO);
    }

    /**
     * {@code PUT  /email-import-documents/:id} : Updates an existing emailImportDocument.
     *
     * @param id the id of the emailImportDocumentDTO to save.
     * @param emailImportDocumentDTO the emailImportDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailImportDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailImportDocumentDTO> updateEmailImportDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailImportDocumentDTO emailImportDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailImportDocument : {}, {}", id, emailImportDocumentDTO);
        if (emailImportDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailImportDocumentDTO = emailImportDocumentService.update(emailImportDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportDocumentDTO.getId().toString()))
            .body(emailImportDocumentDTO);
    }

    /**
     * {@code PATCH  /email-import-documents/:id} : Partial updates given fields of an existing emailImportDocument, field will ignore if it is null
     *
     * @param id the id of the emailImportDocumentDTO to save.
     * @param emailImportDocumentDTO the emailImportDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailImportDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailImportDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailImportDocumentDTO> partialUpdateEmailImportDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailImportDocumentDTO emailImportDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailImportDocument partially : {}, {}", id, emailImportDocumentDTO);
        if (emailImportDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailImportDocumentDTO> result = emailImportDocumentService.partialUpdate(emailImportDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-import-documents} : get all the emailImportDocuments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailImportDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailImportDocumentDTO>> getAllEmailImportDocuments(
        EmailImportDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailImportDocuments by criteria: {}", criteria);

        Page<EmailImportDocumentDTO> page = emailImportDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-import-documents/count} : count all the emailImportDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailImportDocuments(EmailImportDocumentCriteria criteria) {
        LOG.debug("REST request to count EmailImportDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailImportDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-import-documents/:id} : get the "id" emailImportDocument.
     *
     * @param id the id of the emailImportDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailImportDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailImportDocumentDTO> getEmailImportDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailImportDocument : {}", id);
        Optional<EmailImportDocumentDTO> emailImportDocumentDTO = emailImportDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailImportDocumentDTO);
    }

    /**
     * {@code DELETE  /email-import-documents/:id} : delete the "id" emailImportDocument.
     *
     * @param id the id of the emailImportDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailImportDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailImportDocument : {}", id);
        emailImportDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
