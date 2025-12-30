package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
import fr.smartprod.paperdms.document.service.DocumentCommentQueryService;
import fr.smartprod.paperdms.document.service.DocumentCommentService;
import fr.smartprod.paperdms.document.service.criteria.DocumentCommentCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentComment}.
 */
@RestController
@RequestMapping("/api/document-comments")
public class DocumentCommentResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCommentResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentCommentService documentCommentService;

    private final DocumentCommentRepository documentCommentRepository;

    private final DocumentCommentQueryService documentCommentQueryService;

    public DocumentCommentResource(
        DocumentCommentService documentCommentService,
        DocumentCommentRepository documentCommentRepository,
        DocumentCommentQueryService documentCommentQueryService
    ) {
        this.documentCommentService = documentCommentService;
        this.documentCommentRepository = documentCommentRepository;
        this.documentCommentQueryService = documentCommentQueryService;
    }

    /**
     * {@code POST  /document-comments} : Create a new documentComment.
     *
     * @param documentCommentDTO the documentCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentCommentDTO, or with status {@code 400 (Bad Request)} if the documentComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentCommentDTO> createDocumentComment(@Valid @RequestBody DocumentCommentDTO documentCommentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentComment : {}", documentCommentDTO);
        if (documentCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentCommentDTO = documentCommentService.save(documentCommentDTO);
        return ResponseEntity.created(new URI("/api/document-comments/" + documentCommentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentCommentDTO.getId().toString()))
            .body(documentCommentDTO);
    }

    /**
     * {@code PUT  /document-comments/:id} : Updates an existing documentComment.
     *
     * @param id the id of the documentCommentDTO to save.
     * @param documentCommentDTO the documentCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentCommentDTO,
     * or with status {@code 400 (Bad Request)} if the documentCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentCommentDTO> updateDocumentComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentCommentDTO documentCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentComment : {}, {}", id, documentCommentDTO);
        if (documentCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentCommentDTO = documentCommentService.update(documentCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentCommentDTO.getId().toString()))
            .body(documentCommentDTO);
    }

    /**
     * {@code PATCH  /document-comments/:id} : Partial updates given fields of an existing documentComment, field will ignore if it is null
     *
     * @param id the id of the documentCommentDTO to save.
     * @param documentCommentDTO the documentCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentCommentDTO,
     * or with status {@code 400 (Bad Request)} if the documentCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentCommentDTO> partialUpdateDocumentComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentCommentDTO documentCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentComment partially : {}, {}", id, documentCommentDTO);
        if (documentCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentCommentDTO> result = documentCommentService.partialUpdate(documentCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-comments} : get all the documentComments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentComments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentCommentDTO>> getAllDocumentComments(
        DocumentCommentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentComments by criteria: {}", criteria);

        Page<DocumentCommentDTO> page = documentCommentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-comments/count} : count all the documentComments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentComments(DocumentCommentCriteria criteria) {
        LOG.debug("REST request to count DocumentComments by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentCommentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-comments/:id} : get the "id" documentComment.
     *
     * @param id the id of the documentCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentCommentDTO> getDocumentComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentComment : {}", id);
        Optional<DocumentCommentDTO> documentCommentDTO = documentCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentCommentDTO);
    }

    /**
     * {@code DELETE  /document-comments/:id} : delete the "id" documentComment.
     *
     * @param id the id of the documentCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentComment : {}", id);
        documentCommentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-comments/_search?query=:query} : search for the documentComment corresponding
     * to the query.
     *
     * @param query the query of the documentComment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentCommentDTO>> searchDocumentComments(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentComments for query {}", query);
        try {
            Page<DocumentCommentDTO> page = documentCommentService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
