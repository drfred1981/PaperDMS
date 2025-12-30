package fr.smartprod.paperdms.gateway.web.rest;

import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
import fr.smartprod.paperdms.gateway.service.DocumentProcessQueryService;
import fr.smartprod.paperdms.gateway.service.DocumentProcessService;
import fr.smartprod.paperdms.gateway.service.criteria.DocumentProcessCriteria;
import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import fr.smartprod.paperdms.gateway.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.gateway.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.gateway.domain.DocumentProcess}.
 */
@RestController
@RequestMapping("/api/document-processes")
public class DocumentProcessResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessResource.class);

    private static final String ENTITY_NAME = "documentProcess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentProcessService documentProcessService;

    private final DocumentProcessRepository documentProcessRepository;

    private final DocumentProcessQueryService documentProcessQueryService;

    public DocumentProcessResource(
        DocumentProcessService documentProcessService,
        DocumentProcessRepository documentProcessRepository,
        DocumentProcessQueryService documentProcessQueryService
    ) {
        this.documentProcessService = documentProcessService;
        this.documentProcessRepository = documentProcessRepository;
        this.documentProcessQueryService = documentProcessQueryService;
    }

    /**
     * {@code POST  /document-processes} : Create a new documentProcess.
     *
     * @param documentProcessDTO the documentProcessDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentProcessDTO, or with status {@code 400 (Bad Request)} if the documentProcess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentProcessDTO> createDocumentProcess(@Valid @RequestBody DocumentProcessDTO documentProcessDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentProcess : {}", documentProcessDTO);
        if (documentProcessDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentProcessDTO = documentProcessService.save(documentProcessDTO);
        return ResponseEntity.created(new URI("/api/document-processes/" + documentProcessDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentProcessDTO.getId().toString()))
            .body(documentProcessDTO);
    }

    /**
     * {@code PUT  /document-processes/:id} : Updates an existing documentProcess.
     *
     * @param id the id of the documentProcessDTO to save.
     * @param documentProcessDTO the documentProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentProcessDTO,
     * or with status {@code 400 (Bad Request)} if the documentProcessDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentProcessDTO> updateDocumentProcess(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentProcessDTO documentProcessDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentProcess : {}, {}", id, documentProcessDTO);
        if (documentProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentProcessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentProcessDTO = documentProcessService.update(documentProcessDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentProcessDTO.getId().toString()))
            .body(documentProcessDTO);
    }

    /**
     * {@code PATCH  /document-processes/:id} : Partial updates given fields of an existing documentProcess, field will ignore if it is null
     *
     * @param id the id of the documentProcessDTO to save.
     * @param documentProcessDTO the documentProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentProcessDTO,
     * or with status {@code 400 (Bad Request)} if the documentProcessDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentProcessDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentProcessDTO> partialUpdateDocumentProcess(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentProcessDTO documentProcessDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentProcess partially : {}, {}", id, documentProcessDTO);
        if (documentProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentProcessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentProcessDTO> result = documentProcessService.partialUpdate(documentProcessDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentProcessDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-processes} : get all the documentProcesses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentProcesses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentProcessDTO>> getAllDocumentProcesses(
        DocumentProcessCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentProcesses by criteria: {}", criteria);

        Page<DocumentProcessDTO> page = documentProcessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-processes/count} : count all the documentProcesses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentProcesses(DocumentProcessCriteria criteria) {
        LOG.debug("REST request to count DocumentProcesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentProcessQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-processes/:id} : get the "id" documentProcess.
     *
     * @param id the id of the documentProcessDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentProcessDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentProcessDTO> getDocumentProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentProcess : {}", id);
        Optional<DocumentProcessDTO> documentProcessDTO = documentProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentProcessDTO);
    }

    /**
     * {@code DELETE  /document-processes/:id} : delete the "id" documentProcess.
     *
     * @param id the id of the documentProcessDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentProcess : {}", id);
        documentProcessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-processes/_search?query=:query} : search for the documentProcess corresponding
     * to the query.
     *
     * @param query the query of the documentProcess search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentProcessDTO>> searchDocumentProcesses(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentProcesses for query {}", query);
        try {
            Page<DocumentProcessDTO> page = documentProcessService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
