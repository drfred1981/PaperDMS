package com.ged.similarity.web.rest;

import com.ged.similarity.repository.DocumentFingerprintRepository;
import com.ged.similarity.service.DocumentFingerprintService;
import com.ged.similarity.service.dto.DocumentFingerprintDTO;
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
 * REST controller for managing {@link com.ged.similarity.domain.DocumentFingerprint}.
 */
@RestController
@RequestMapping("/api/document-fingerprints")
public class DocumentFingerprintResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFingerprintResource.class);

    private static final String ENTITY_NAME = "similarityServiceDocumentFingerprint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentFingerprintService documentFingerprintService;

    private final DocumentFingerprintRepository documentFingerprintRepository;

    public DocumentFingerprintResource(
        DocumentFingerprintService documentFingerprintService,
        DocumentFingerprintRepository documentFingerprintRepository
    ) {
        this.documentFingerprintService = documentFingerprintService;
        this.documentFingerprintRepository = documentFingerprintRepository;
    }

    /**
     * {@code POST  /document-fingerprints} : Create a new documentFingerprint.
     *
     * @param documentFingerprintDTO the documentFingerprintDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentFingerprintDTO, or with status {@code 400 (Bad Request)} if the documentFingerprint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentFingerprintDTO> createDocumentFingerprint(
        @Valid @RequestBody DocumentFingerprintDTO documentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DocumentFingerprint : {}", documentFingerprintDTO);
        if (documentFingerprintDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentFingerprint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentFingerprintDTO = documentFingerprintService.save(documentFingerprintDTO);
        return ResponseEntity.created(new URI("/api/document-fingerprints/" + documentFingerprintDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentFingerprintDTO.getId().toString()))
            .body(documentFingerprintDTO);
    }

    /**
     * {@code PUT  /document-fingerprints/:id} : Updates an existing documentFingerprint.
     *
     * @param id the id of the documentFingerprintDTO to save.
     * @param documentFingerprintDTO the documentFingerprintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentFingerprintDTO,
     * or with status {@code 400 (Bad Request)} if the documentFingerprintDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentFingerprintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentFingerprintDTO> updateDocumentFingerprint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentFingerprintDTO documentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentFingerprint : {}, {}", id, documentFingerprintDTO);
        if (documentFingerprintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentFingerprintDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentFingerprintRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentFingerprintDTO = documentFingerprintService.update(documentFingerprintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentFingerprintDTO.getId().toString()))
            .body(documentFingerprintDTO);
    }

    /**
     * {@code PATCH  /document-fingerprints/:id} : Partial updates given fields of an existing documentFingerprint, field will ignore if it is null
     *
     * @param id the id of the documentFingerprintDTO to save.
     * @param documentFingerprintDTO the documentFingerprintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentFingerprintDTO,
     * or with status {@code 400 (Bad Request)} if the documentFingerprintDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentFingerprintDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentFingerprintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentFingerprintDTO> partialUpdateDocumentFingerprint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentFingerprintDTO documentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentFingerprint partially : {}, {}", id, documentFingerprintDTO);
        if (documentFingerprintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentFingerprintDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentFingerprintRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentFingerprintDTO> result = documentFingerprintService.partialUpdate(documentFingerprintDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentFingerprintDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-fingerprints} : get all the documentFingerprints.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentFingerprints in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentFingerprintDTO>> getAllDocumentFingerprints(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentFingerprints");
        Page<DocumentFingerprintDTO> page = documentFingerprintService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-fingerprints/:id} : get the "id" documentFingerprint.
     *
     * @param id the id of the documentFingerprintDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentFingerprintDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentFingerprintDTO> getDocumentFingerprint(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentFingerprint : {}", id);
        Optional<DocumentFingerprintDTO> documentFingerprintDTO = documentFingerprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentFingerprintDTO);
    }

    /**
     * {@code DELETE  /document-fingerprints/:id} : delete the "id" documentFingerprint.
     *
     * @param id the id of the documentFingerprintDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentFingerprint(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentFingerprint : {}", id);
        documentFingerprintService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
