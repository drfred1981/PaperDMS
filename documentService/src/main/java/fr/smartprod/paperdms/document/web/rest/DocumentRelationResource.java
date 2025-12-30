package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
import fr.smartprod.paperdms.document.service.DocumentRelationQueryService;
import fr.smartprod.paperdms.document.service.DocumentRelationService;
import fr.smartprod.paperdms.document.service.criteria.DocumentRelationCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentRelation}.
 */
@RestController
@RequestMapping("/api/document-relations")
public class DocumentRelationResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRelationResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentRelationService documentRelationService;

    private final DocumentRelationRepository documentRelationRepository;

    private final DocumentRelationQueryService documentRelationQueryService;

    public DocumentRelationResource(
        DocumentRelationService documentRelationService,
        DocumentRelationRepository documentRelationRepository,
        DocumentRelationQueryService documentRelationQueryService
    ) {
        this.documentRelationService = documentRelationService;
        this.documentRelationRepository = documentRelationRepository;
        this.documentRelationQueryService = documentRelationQueryService;
    }

    /**
     * {@code POST  /document-relations} : Create a new documentRelation.
     *
     * @param documentRelationDTO the documentRelationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentRelationDTO, or with status {@code 400 (Bad Request)} if the documentRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentRelationDTO> createDocumentRelation(@Valid @RequestBody DocumentRelationDTO documentRelationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentRelation : {}", documentRelationDTO);
        if (documentRelationDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentRelationDTO = documentRelationService.save(documentRelationDTO);
        return ResponseEntity.created(new URI("/api/document-relations/" + documentRelationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentRelationDTO.getId().toString()))
            .body(documentRelationDTO);
    }

    /**
     * {@code PUT  /document-relations/:id} : Updates an existing documentRelation.
     *
     * @param id the id of the documentRelationDTO to save.
     * @param documentRelationDTO the documentRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentRelationDTO,
     * or with status {@code 400 (Bad Request)} if the documentRelationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentRelationDTO> updateDocumentRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentRelationDTO documentRelationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentRelation : {}, {}", id, documentRelationDTO);
        if (documentRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentRelationDTO = documentRelationService.update(documentRelationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentRelationDTO.getId().toString()))
            .body(documentRelationDTO);
    }

    /**
     * {@code PATCH  /document-relations/:id} : Partial updates given fields of an existing documentRelation, field will ignore if it is null
     *
     * @param id the id of the documentRelationDTO to save.
     * @param documentRelationDTO the documentRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentRelationDTO,
     * or with status {@code 400 (Bad Request)} if the documentRelationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentRelationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentRelationDTO> partialUpdateDocumentRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentRelationDTO documentRelationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentRelation partially : {}, {}", id, documentRelationDTO);
        if (documentRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentRelationDTO> result = documentRelationService.partialUpdate(documentRelationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentRelationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-relations} : get all the documentRelations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentRelations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentRelationDTO>> getAllDocumentRelations(
        DocumentRelationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentRelations by criteria: {}", criteria);

        Page<DocumentRelationDTO> page = documentRelationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-relations/count} : count all the documentRelations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentRelations(DocumentRelationCriteria criteria) {
        LOG.debug("REST request to count DocumentRelations by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentRelationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-relations/:id} : get the "id" documentRelation.
     *
     * @param id the id of the documentRelationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentRelationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentRelationDTO> getDocumentRelation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentRelation : {}", id);
        Optional<DocumentRelationDTO> documentRelationDTO = documentRelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentRelationDTO);
    }

    /**
     * {@code DELETE  /document-relations/:id} : delete the "id" documentRelation.
     *
     * @param id the id of the documentRelationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentRelation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentRelation : {}", id);
        documentRelationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-relations/_search?query=:query} : search for the documentRelation corresponding
     * to the query.
     *
     * @param query the query of the documentRelation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentRelationDTO>> searchDocumentRelations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentRelations for query {}", query);
        try {
            Page<DocumentRelationDTO> page = documentRelationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
