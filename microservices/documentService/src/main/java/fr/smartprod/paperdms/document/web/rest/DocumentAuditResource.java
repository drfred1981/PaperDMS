package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import fr.smartprod.paperdms.document.service.DocumentAuditQueryService;
import fr.smartprod.paperdms.document.service.DocumentAuditService;
import fr.smartprod.paperdms.document.service.criteria.DocumentAuditCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.document.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentAudit}.
 */
@RestController
@RequestMapping("/api/document-audits")
public class DocumentAuditResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAuditResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentAuditService documentAuditService;

    private final DocumentAuditRepository documentAuditRepository;

    private final DocumentAuditQueryService documentAuditQueryService;

    public DocumentAuditResource(
        DocumentAuditService documentAuditService,
        DocumentAuditRepository documentAuditRepository,
        DocumentAuditQueryService documentAuditQueryService
    ) {
        this.documentAuditService = documentAuditService;
        this.documentAuditRepository = documentAuditRepository;
        this.documentAuditQueryService = documentAuditQueryService;
    }

    /**
     * {@code POST  /document-audits} : Create a new documentAudit.
     *
     * @param documentAuditDTO the documentAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentAuditDTO, or with status {@code 400 (Bad Request)} if the documentAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentAuditDTO> createDocumentAudit(@Valid @RequestBody DocumentAuditDTO documentAuditDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentAudit : {}", documentAuditDTO);
        if (documentAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentAuditDTO = documentAuditService.save(documentAuditDTO);
        return ResponseEntity.created(new URI("/api/document-audits/" + documentAuditDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentAuditDTO.getId().toString()))
            .body(documentAuditDTO);
    }

    /**
     * {@code PUT  /document-audits/:id} : Updates an existing documentAudit.
     *
     * @param id the id of the documentAuditDTO to save.
     * @param documentAuditDTO the documentAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentAuditDTO,
     * or with status {@code 400 (Bad Request)} if the documentAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentAuditDTO> updateDocumentAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentAuditDTO documentAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentAudit : {}, {}", id, documentAuditDTO);
        if (documentAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentAuditDTO = documentAuditService.update(documentAuditDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentAuditDTO.getId().toString()))
            .body(documentAuditDTO);
    }

    /**
     * {@code PATCH  /document-audits/:id} : Partial updates given fields of an existing documentAudit, field will ignore if it is null
     *
     * @param id the id of the documentAuditDTO to save.
     * @param documentAuditDTO the documentAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentAuditDTO,
     * or with status {@code 400 (Bad Request)} if the documentAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentAuditDTO> partialUpdateDocumentAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentAuditDTO documentAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentAudit partially : {}, {}", id, documentAuditDTO);
        if (documentAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentAuditDTO> result = documentAuditService.partialUpdate(documentAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-audits} : get all the documentAudits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentAudits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentAuditDTO>> getAllDocumentAudits(
        DocumentAuditCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentAudits by criteria: {}", criteria);

        Page<DocumentAuditDTO> page = documentAuditQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-audits/count} : count all the documentAudits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentAudits(DocumentAuditCriteria criteria) {
        LOG.debug("REST request to count DocumentAudits by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentAuditQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-audits/:id} : get the "id" documentAudit.
     *
     * @param id the id of the documentAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentAuditDTO> getDocumentAudit(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentAudit : {}", id);
        Optional<DocumentAuditDTO> documentAuditDTO = documentAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentAuditDTO);
    }

    /**
     * {@code DELETE  /document-audits/:id} : delete the "id" documentAudit.
     *
     * @param id the id of the documentAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentAudit(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentAudit : {}", id);
        documentAuditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-audits/_search?query=:query} : search for the documentAudit corresponding
     * to the query.
     *
     * @param query the query of the documentAudit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentAuditDTO>> searchDocumentAudits(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentAudits for query {}", query);
        try {
            Page<DocumentAuditDTO> page = documentAuditService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
