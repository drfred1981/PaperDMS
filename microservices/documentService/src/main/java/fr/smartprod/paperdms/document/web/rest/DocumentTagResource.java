package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.service.DocumentTagQueryService;
import fr.smartprod.paperdms.document.service.DocumentTagService;
import fr.smartprod.paperdms.document.service.criteria.DocumentTagCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentTag}.
 */
@RestController
@RequestMapping("/api/document-tags")
public class DocumentTagResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTagResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTagService documentTagService;

    private final DocumentTagRepository documentTagRepository;

    private final DocumentTagQueryService documentTagQueryService;

    public DocumentTagResource(
        DocumentTagService documentTagService,
        DocumentTagRepository documentTagRepository,
        DocumentTagQueryService documentTagQueryService
    ) {
        this.documentTagService = documentTagService;
        this.documentTagRepository = documentTagRepository;
        this.documentTagQueryService = documentTagQueryService;
    }

    /**
     * {@code POST  /document-tags} : Create a new documentTag.
     *
     * @param documentTagDTO the documentTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentTagDTO, or with status {@code 400 (Bad Request)} if the documentTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentTagDTO> createDocumentTag(@Valid @RequestBody DocumentTagDTO documentTagDTO) throws URISyntaxException {
        LOG.debug("REST request to save DocumentTag : {}", documentTagDTO);
        if (documentTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentTagDTO = documentTagService.save(documentTagDTO);
        return ResponseEntity.created(new URI("/api/document-tags/" + documentTagDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentTagDTO.getId().toString()))
            .body(documentTagDTO);
    }

    /**
     * {@code PUT  /document-tags/:id} : Updates an existing documentTag.
     *
     * @param id the id of the documentTagDTO to save.
     * @param documentTagDTO the documentTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTagDTO,
     * or with status {@code 400 (Bad Request)} if the documentTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTagDTO> updateDocumentTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentTagDTO documentTagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentTag : {}, {}", id, documentTagDTO);
        if (documentTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentTagDTO = documentTagService.update(documentTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTagDTO.getId().toString()))
            .body(documentTagDTO);
    }

    /**
     * {@code PATCH  /document-tags/:id} : Partial updates given fields of an existing documentTag, field will ignore if it is null
     *
     * @param id the id of the documentTagDTO to save.
     * @param documentTagDTO the documentTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTagDTO,
     * or with status {@code 400 (Bad Request)} if the documentTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentTagDTO> partialUpdateDocumentTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentTagDTO documentTagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentTag partially : {}, {}", id, documentTagDTO);
        if (documentTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentTagDTO> result = documentTagService.partialUpdate(documentTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-tags} : get all the documentTags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentTagDTO>> getAllDocumentTags(
        DocumentTagCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentTags by criteria: {}", criteria);

        Page<DocumentTagDTO> page = documentTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-tags/count} : count all the documentTags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentTags(DocumentTagCriteria criteria) {
        LOG.debug("REST request to count DocumentTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentTagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-tags/:id} : get the "id" documentTag.
     *
     * @param id the id of the documentTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTagDTO> getDocumentTag(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentTag : {}", id);
        Optional<DocumentTagDTO> documentTagDTO = documentTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentTagDTO);
    }

    /**
     * {@code DELETE  /document-tags/:id} : delete the "id" documentTag.
     *
     * @param id the id of the documentTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentTag(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentTag : {}", id);
        documentTagService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-tags/_search?query=:query} : search for the documentTag corresponding
     * to the query.
     *
     * @param query the query of the documentTag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentTagDTO>> searchDocumentTags(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentTags for query {}", query);
        try {
            Page<DocumentTagDTO> page = documentTagService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
