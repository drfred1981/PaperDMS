package com.ged.similarity.web.rest;

import com.ged.similarity.repository.DocumentSimilarityRepository;
import com.ged.similarity.service.DocumentSimilarityQueryService;
import com.ged.similarity.service.DocumentSimilarityService;
import com.ged.similarity.service.criteria.DocumentSimilarityCriteria;
import com.ged.similarity.service.dto.DocumentSimilarityDTO;
import com.ged.similarity.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ged.similarity.domain.DocumentSimilarity}.
 */
@RestController
@RequestMapping("/api/document-similarities")
public class DocumentSimilarityResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSimilarityResource.class);

    private static final String ENTITY_NAME = "similarityServiceDocumentSimilarity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentSimilarityService documentSimilarityService;

    private final DocumentSimilarityRepository documentSimilarityRepository;

    private final DocumentSimilarityQueryService documentSimilarityQueryService;

    public DocumentSimilarityResource(
        DocumentSimilarityService documentSimilarityService,
        DocumentSimilarityRepository documentSimilarityRepository,
        DocumentSimilarityQueryService documentSimilarityQueryService
    ) {
        this.documentSimilarityService = documentSimilarityService;
        this.documentSimilarityRepository = documentSimilarityRepository;
        this.documentSimilarityQueryService = documentSimilarityQueryService;
    }

    /**
     * {@code POST  /document-similarities} : Create a new documentSimilarity.
     *
     * @param documentSimilarityDTO the documentSimilarityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentSimilarityDTO, or with status {@code 400 (Bad Request)} if the documentSimilarity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentSimilarityDTO> createDocumentSimilarity(@Valid @RequestBody DocumentSimilarityDTO documentSimilarityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentSimilarity : {}", documentSimilarityDTO);
        if (documentSimilarityDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentSimilarity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentSimilarityDTO = documentSimilarityService.save(documentSimilarityDTO);
        return ResponseEntity.created(new URI("/api/document-similarities/" + documentSimilarityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentSimilarityDTO.getId().toString()))
            .body(documentSimilarityDTO);
    }

    /**
     * {@code PUT  /document-similarities/:id} : Updates an existing documentSimilarity.
     *
     * @param id the id of the documentSimilarityDTO to save.
     * @param documentSimilarityDTO the documentSimilarityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSimilarityDTO,
     * or with status {@code 400 (Bad Request)} if the documentSimilarityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentSimilarityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentSimilarityDTO> updateDocumentSimilarity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentSimilarityDTO documentSimilarityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentSimilarity : {}, {}", id, documentSimilarityDTO);
        if (documentSimilarityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSimilarityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSimilarityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentSimilarityDTO = documentSimilarityService.update(documentSimilarityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentSimilarityDTO.getId().toString()))
            .body(documentSimilarityDTO);
    }

    /**
     * {@code PATCH  /document-similarities/:id} : Partial updates given fields of an existing documentSimilarity, field will ignore if it is null
     *
     * @param id the id of the documentSimilarityDTO to save.
     * @param documentSimilarityDTO the documentSimilarityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSimilarityDTO,
     * or with status {@code 400 (Bad Request)} if the documentSimilarityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentSimilarityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentSimilarityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentSimilarityDTO> partialUpdateDocumentSimilarity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentSimilarityDTO documentSimilarityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentSimilarity partially : {}, {}", id, documentSimilarityDTO);
        if (documentSimilarityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSimilarityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSimilarityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentSimilarityDTO> result = documentSimilarityService.partialUpdate(documentSimilarityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentSimilarityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-similarities} : get all the documentSimilarities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentSimilarities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentSimilarityDTO>> getAllDocumentSimilarities(
        DocumentSimilarityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentSimilarities by criteria: {}", criteria);

        Page<DocumentSimilarityDTO> page = documentSimilarityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-similarities/count} : count all the documentSimilarities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentSimilarities(DocumentSimilarityCriteria criteria) {
        LOG.debug("REST request to count DocumentSimilarities by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentSimilarityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-similarities/:id} : get the "id" documentSimilarity.
     *
     * @param id the id of the documentSimilarityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentSimilarityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentSimilarityDTO> getDocumentSimilarity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentSimilarity : {}", id);
        Optional<DocumentSimilarityDTO> documentSimilarityDTO = documentSimilarityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentSimilarityDTO);
    }

    /**
     * {@code DELETE  /document-similarities/:id} : delete the "id" documentSimilarity.
     *
     * @param id the id of the documentSimilarityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentSimilarity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentSimilarity : {}", id);
        documentSimilarityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
