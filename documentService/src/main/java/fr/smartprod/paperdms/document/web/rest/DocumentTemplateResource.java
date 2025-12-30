package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.service.DocumentTemplateQueryService;
import fr.smartprod.paperdms.document.service.DocumentTemplateService;
import fr.smartprod.paperdms.document.service.criteria.DocumentTemplateCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentTemplate}.
 */
@RestController
@RequestMapping("/api/document-templates")
public class DocumentTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTemplateResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTemplateService documentTemplateService;

    private final DocumentTemplateRepository documentTemplateRepository;

    private final DocumentTemplateQueryService documentTemplateQueryService;

    public DocumentTemplateResource(
        DocumentTemplateService documentTemplateService,
        DocumentTemplateRepository documentTemplateRepository,
        DocumentTemplateQueryService documentTemplateQueryService
    ) {
        this.documentTemplateService = documentTemplateService;
        this.documentTemplateRepository = documentTemplateRepository;
        this.documentTemplateQueryService = documentTemplateQueryService;
    }

    /**
     * {@code POST  /document-templates} : Create a new documentTemplate.
     *
     * @param documentTemplateDTO the documentTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentTemplateDTO, or with status {@code 400 (Bad Request)} if the documentTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentTemplateDTO> createDocumentTemplate(@Valid @RequestBody DocumentTemplateDTO documentTemplateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentTemplate : {}", documentTemplateDTO);
        if (documentTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentTemplateDTO = documentTemplateService.save(documentTemplateDTO);
        return ResponseEntity.created(new URI("/api/document-templates/" + documentTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentTemplateDTO.getId().toString()))
            .body(documentTemplateDTO);
    }

    /**
     * {@code PUT  /document-templates/:id} : Updates an existing documentTemplate.
     *
     * @param id the id of the documentTemplateDTO to save.
     * @param documentTemplateDTO the documentTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the documentTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTemplateDTO> updateDocumentTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentTemplateDTO documentTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentTemplate : {}, {}", id, documentTemplateDTO);
        if (documentTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentTemplateDTO = documentTemplateService.update(documentTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTemplateDTO.getId().toString()))
            .body(documentTemplateDTO);
    }

    /**
     * {@code PATCH  /document-templates/:id} : Partial updates given fields of an existing documentTemplate, field will ignore if it is null
     *
     * @param id the id of the documentTemplateDTO to save.
     * @param documentTemplateDTO the documentTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the documentTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentTemplateDTO> partialUpdateDocumentTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentTemplateDTO documentTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentTemplate partially : {}, {}", id, documentTemplateDTO);
        if (documentTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentTemplateDTO> result = documentTemplateService.partialUpdate(documentTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-templates} : get all the documentTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentTemplateDTO>> getAllDocumentTemplates(
        DocumentTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentTemplates by criteria: {}", criteria);

        Page<DocumentTemplateDTO> page = documentTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-templates/count} : count all the documentTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentTemplates(DocumentTemplateCriteria criteria) {
        LOG.debug("REST request to count DocumentTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-templates/:id} : get the "id" documentTemplate.
     *
     * @param id the id of the documentTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTemplateDTO> getDocumentTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentTemplate : {}", id);
        Optional<DocumentTemplateDTO> documentTemplateDTO = documentTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentTemplateDTO);
    }

    /**
     * {@code DELETE  /document-templates/:id} : delete the "id" documentTemplate.
     *
     * @param id the id of the documentTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentTemplate : {}", id);
        documentTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /document-templates/_search?query=:query} : search for the documentTemplate corresponding
     * to the query.
     *
     * @param query the query of the documentTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentTemplateDTO>> searchDocumentTemplates(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DocumentTemplates for query {}", query);
        try {
            Page<DocumentTemplateDTO> page = documentTemplateService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
