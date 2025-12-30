package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentExtractedFieldRepository;
import fr.smartprod.paperdms.document.service.DocumentExtractedFieldQueryService;
import fr.smartprod.paperdms.document.service.DocumentExtractedFieldService;
import fr.smartprod.paperdms.document.service.criteria.DocumentExtractedFieldCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentExtractedFieldDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentExtractedField}.
 */
@RestController
@RequestMapping("/api/document-extracted-fields")
public class DocumentExtractedFieldResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentExtractedFieldResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentExtractedField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentExtractedFieldService documentExtractedFieldService;

    private final DocumentExtractedFieldRepository documentExtractedFieldRepository;

    private final DocumentExtractedFieldQueryService documentExtractedFieldQueryService;

    public DocumentExtractedFieldResource(
        DocumentExtractedFieldService documentExtractedFieldService,
        DocumentExtractedFieldRepository documentExtractedFieldRepository,
        DocumentExtractedFieldQueryService documentExtractedFieldQueryService
    ) {
        this.documentExtractedFieldService = documentExtractedFieldService;
        this.documentExtractedFieldRepository = documentExtractedFieldRepository;
        this.documentExtractedFieldQueryService = documentExtractedFieldQueryService;
    }

    /**
     * {@code POST  /document-extracted-fields} : Create a new documentExtractedField.
     *
     * @param documentExtractedFieldDTO the documentExtractedFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentExtractedFieldDTO, or with status {@code 400 (Bad Request)} if the documentExtractedField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentExtractedFieldDTO> createDocumentExtractedField(
        @Valid @RequestBody DocumentExtractedFieldDTO documentExtractedFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DocumentExtractedField : {}", documentExtractedFieldDTO);
        if (documentExtractedFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentExtractedField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentExtractedFieldDTO = documentExtractedFieldService.save(documentExtractedFieldDTO);
        return ResponseEntity.created(new URI("/api/document-extracted-fields/" + documentExtractedFieldDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentExtractedFieldDTO.getId().toString()))
            .body(documentExtractedFieldDTO);
    }

    /**
     * {@code PUT  /document-extracted-fields/:id} : Updates an existing documentExtractedField.
     *
     * @param id the id of the documentExtractedFieldDTO to save.
     * @param documentExtractedFieldDTO the documentExtractedFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentExtractedFieldDTO,
     * or with status {@code 400 (Bad Request)} if the documentExtractedFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentExtractedFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentExtractedFieldDTO> updateDocumentExtractedField(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentExtractedFieldDTO documentExtractedFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentExtractedField : {}, {}", id, documentExtractedFieldDTO);
        if (documentExtractedFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentExtractedFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentExtractedFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentExtractedFieldDTO = documentExtractedFieldService.update(documentExtractedFieldDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentExtractedFieldDTO.getId().toString()))
            .body(documentExtractedFieldDTO);
    }

    /**
     * {@code PATCH  /document-extracted-fields/:id} : Partial updates given fields of an existing documentExtractedField, field will ignore if it is null
     *
     * @param id the id of the documentExtractedFieldDTO to save.
     * @param documentExtractedFieldDTO the documentExtractedFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentExtractedFieldDTO,
     * or with status {@code 400 (Bad Request)} if the documentExtractedFieldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentExtractedFieldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentExtractedFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentExtractedFieldDTO> partialUpdateDocumentExtractedField(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentExtractedFieldDTO documentExtractedFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentExtractedField partially : {}, {}", id, documentExtractedFieldDTO);
        if (documentExtractedFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentExtractedFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentExtractedFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentExtractedFieldDTO> result = documentExtractedFieldService.partialUpdate(documentExtractedFieldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentExtractedFieldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-extracted-fields} : get all the documentExtractedFields.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentExtractedFields in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentExtractedFieldDTO>> getAllDocumentExtractedFields(
        DocumentExtractedFieldCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentExtractedFields by criteria: {}", criteria);

        Page<DocumentExtractedFieldDTO> page = documentExtractedFieldQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-extracted-fields/count} : count all the documentExtractedFields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentExtractedFields(DocumentExtractedFieldCriteria criteria) {
        LOG.debug("REST request to count DocumentExtractedFields by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentExtractedFieldQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-extracted-fields/:id} : get the "id" documentExtractedField.
     *
     * @param id the id of the documentExtractedFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentExtractedFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentExtractedFieldDTO> getDocumentExtractedField(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentExtractedField : {}", id);
        Optional<DocumentExtractedFieldDTO> documentExtractedFieldDTO = documentExtractedFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentExtractedFieldDTO);
    }

    /**
     * {@code DELETE  /document-extracted-fields/:id} : delete the "id" documentExtractedField.
     *
     * @param id the id of the documentExtractedFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentExtractedField(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentExtractedField : {}", id);
        documentExtractedFieldService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-extracted-fields/_search?query=:query} : search for the documentExtractedField corresponding
     * to the query.
     *
     * @param query the query of the documentExtractedField search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentExtractedFieldDTO>> searchDocumentExtractedFields(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentExtractedFields for query {}", query);
        try {
            Page<DocumentExtractedFieldDTO> page = documentExtractedFieldService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
