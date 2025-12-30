package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.service.DocumentVersionQueryService;
import fr.smartprod.paperdms.document.service.DocumentVersionService;
import fr.smartprod.paperdms.document.service.criteria.DocumentVersionCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentVersion}.
 */
@RestController
@RequestMapping("/api/document-versions")
public class DocumentVersionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentVersionResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentVersionService documentVersionService;

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentVersionQueryService documentVersionQueryService;

    public DocumentVersionResource(
        DocumentVersionService documentVersionService,
        DocumentVersionRepository documentVersionRepository,
        DocumentVersionQueryService documentVersionQueryService
    ) {
        this.documentVersionService = documentVersionService;
        this.documentVersionRepository = documentVersionRepository;
        this.documentVersionQueryService = documentVersionQueryService;
    }

    /**
     * {@code POST  /document-versions} : Create a new documentVersion.
     *
     * @param documentVersionDTO the documentVersionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentVersionDTO, or with status {@code 400 (Bad Request)} if the documentVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentVersionDTO> createDocumentVersion(@Valid @RequestBody DocumentVersionDTO documentVersionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentVersion : {}", documentVersionDTO);
        if (documentVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentVersionDTO = documentVersionService.save(documentVersionDTO);
        return ResponseEntity.created(new URI("/api/document-versions/" + documentVersionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentVersionDTO.getId().toString()))
            .body(documentVersionDTO);
    }

    /**
     * {@code PUT  /document-versions/:id} : Updates an existing documentVersion.
     *
     * @param id the id of the documentVersionDTO to save.
     * @param documentVersionDTO the documentVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentVersionDTO,
     * or with status {@code 400 (Bad Request)} if the documentVersionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentVersionDTO> updateDocumentVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentVersionDTO documentVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentVersion : {}, {}", id, documentVersionDTO);
        if (documentVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentVersionDTO = documentVersionService.update(documentVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentVersionDTO.getId().toString()))
            .body(documentVersionDTO);
    }

    /**
     * {@code PATCH  /document-versions/:id} : Partial updates given fields of an existing documentVersion, field will ignore if it is null
     *
     * @param id the id of the documentVersionDTO to save.
     * @param documentVersionDTO the documentVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentVersionDTO,
     * or with status {@code 400 (Bad Request)} if the documentVersionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentVersionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentVersionDTO> partialUpdateDocumentVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentVersionDTO documentVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentVersion partially : {}, {}", id, documentVersionDTO);
        if (documentVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentVersionDTO> result = documentVersionService.partialUpdate(documentVersionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentVersionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-versions} : get all the documentVersions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentVersions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentVersionDTO>> getAllDocumentVersions(
        DocumentVersionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentVersions by criteria: {}", criteria);

        Page<DocumentVersionDTO> page = documentVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-versions/count} : count all the documentVersions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentVersions(DocumentVersionCriteria criteria) {
        LOG.debug("REST request to count DocumentVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentVersionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-versions/:id} : get the "id" documentVersion.
     *
     * @param id the id of the documentVersionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentVersionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentVersionDTO> getDocumentVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentVersion : {}", id);
        Optional<DocumentVersionDTO> documentVersionDTO = documentVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentVersionDTO);
    }

    /**
     * {@code DELETE  /document-versions/:id} : delete the "id" documentVersion.
     *
     * @param id the id of the documentVersionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentVersion : {}", id);
        documentVersionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-versions/_search?query=:query} : search for the documentVersion corresponding
     * to the query.
     *
     * @param query the query of the documentVersion search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentVersionDTO>> searchDocumentVersions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentVersions for query {}", query);
        try {
            Page<DocumentVersionDTO> page = documentVersionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
