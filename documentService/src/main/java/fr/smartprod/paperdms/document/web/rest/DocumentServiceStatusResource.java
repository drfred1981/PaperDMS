package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentServiceStatusUploadRepository;
import fr.smartprod.paperdms.document.service.DocumentServiceStatusQueryService;
import fr.smartprod.paperdms.document.service.DocumentServiceStatusService;
import fr.smartprod.paperdms.document.service.criteria.DocumentServiceStatusCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus}.
 */
@RestController
@RequestMapping("/api/document-service-statuses")
public class DocumentServiceStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceStatusResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentServiceStatus";

    @Value("${jhipster.clientApp.name:documentService}")
    private String applicationName;

    private final DocumentServiceStatusService documentServiceStatusService;

    private final DocumentServiceStatusUploadRepository documentServiceStatusRepository;

    private final DocumentServiceStatusQueryService documentServiceStatusQueryService;

    public DocumentServiceStatusResource(
        DocumentServiceStatusService documentServiceStatusService,
        DocumentServiceStatusUploadRepository documentServiceStatusRepository,
        DocumentServiceStatusQueryService documentServiceStatusQueryService
    ) {
        this.documentServiceStatusService = documentServiceStatusService;
        this.documentServiceStatusRepository = documentServiceStatusRepository;
        this.documentServiceStatusQueryService = documentServiceStatusQueryService;
    }

    /**
     * {@code POST  /document-service-statuses} : Create a new documentServiceStatus.
     *
     * @param documentServiceStatusDTO the documentServiceStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentServiceStatusDTO, or with status {@code 400 (Bad Request)} if the documentServiceStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentServiceStatusDTO> createDocumentServiceStatus(
        @Valid @RequestBody DocumentServiceStatusDTO documentServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DocumentServiceStatus : {}", documentServiceStatusDTO);
        if (documentServiceStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentServiceStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentServiceStatusDTO = documentServiceStatusService.save(documentServiceStatusDTO);
        return ResponseEntity.created(new URI("/api/document-service-statuses/" + documentServiceStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentServiceStatusDTO.getId().toString()))
            .body(documentServiceStatusDTO);
    }

    /**
     * {@code PUT  /document-service-statuses/:id} : Updates an existing documentServiceStatus.
     *
     * @param id the id of the documentServiceStatusDTO to save.
     * @param documentServiceStatusDTO the documentServiceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentServiceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the documentServiceStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentServiceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentServiceStatusDTO> updateDocumentServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentServiceStatusDTO documentServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentServiceStatus : {}, {}", id, documentServiceStatusDTO);
        if (documentServiceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentServiceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentServiceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentServiceStatusDTO = documentServiceStatusService.update(documentServiceStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentServiceStatusDTO.getId().toString()))
            .body(documentServiceStatusDTO);
    }

    /**
     * {@code PATCH  /document-service-statuses/:id} : Partial updates given fields of an existing documentServiceStatus, field will ignore if it is null
     *
     * @param id the id of the documentServiceStatusDTO to save.
     * @param documentServiceStatusDTO the documentServiceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentServiceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the documentServiceStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentServiceStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentServiceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentServiceStatusDTO> partialUpdateDocumentServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentServiceStatusDTO documentServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentServiceStatus partially : {}, {}", id, documentServiceStatusDTO);
        if (documentServiceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentServiceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentServiceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentServiceStatusDTO> result = documentServiceStatusService.partialUpdate(documentServiceStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentServiceStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-service-statuses} : get all the documentServiceStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentServiceStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentServiceStatusDTO>> getAllDocumentServiceStatuses(
        DocumentServiceStatusCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentServiceStatuses by criteria: {}", criteria);

        Page<DocumentServiceStatusDTO> page = documentServiceStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-service-statuses/count} : count all the documentServiceStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentServiceStatuses(DocumentServiceStatusCriteria criteria) {
        LOG.debug("REST request to count DocumentServiceStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentServiceStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-service-statuses/:id} : get the "id" documentServiceStatus.
     *
     * @param id the id of the documentServiceStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentServiceStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentServiceStatusDTO> getDocumentServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentServiceStatus : {}", id);
        Optional<DocumentServiceStatusDTO> documentServiceStatusDTO = documentServiceStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentServiceStatusDTO);
    }

    /**
     * {@code DELETE  /document-service-statuses/:id} : delete the "id" documentServiceStatus.
     *
     * @param id the id of the documentServiceStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentServiceStatus : {}", id);
        documentServiceStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-service-statuses/_search?query=:query} : search for the documentServiceStatus corresponding
     * to the query.
     *
     * @param query the query of the documentServiceStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentServiceStatusDTO>> searchDocumentServiceStatuses(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentServiceStatuses for query {}", query);
        try {
            Page<DocumentServiceStatusDTO> page = documentServiceStatusService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
