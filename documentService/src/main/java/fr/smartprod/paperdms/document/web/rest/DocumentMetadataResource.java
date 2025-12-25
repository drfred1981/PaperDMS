package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentMetadataRepository;
import fr.smartprod.paperdms.document.service.DocumentMetadataService;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentMetadata}.
 */
@RestController
@RequestMapping("/api/document-metadata")
public class DocumentMetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentMetadataResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentMetadataService documentMetadataService;

    private final DocumentMetadataRepository documentMetadataRepository;

    public DocumentMetadataResource(
        DocumentMetadataService documentMetadataService,
        DocumentMetadataRepository documentMetadataRepository
    ) {
        this.documentMetadataService = documentMetadataService;
        this.documentMetadataRepository = documentMetadataRepository;
    }

    /**
     * {@code POST  /document-metadata} : Create a new documentMetadata.
     *
     * @param documentMetadataDTO the documentMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentMetadataDTO, or with status {@code 400 (Bad Request)} if the documentMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentMetadataDTO> createDocumentMetadata(@Valid @RequestBody DocumentMetadataDTO documentMetadataDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentMetadata : {}", documentMetadataDTO);
        if (documentMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentMetadataDTO = documentMetadataService.save(documentMetadataDTO);
        return ResponseEntity.created(new URI("/api/document-metadata/" + documentMetadataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentMetadataDTO.getId().toString()))
            .body(documentMetadataDTO);
    }

    /**
     * {@code PUT  /document-metadata/:id} : Updates an existing documentMetadata.
     *
     * @param id the id of the documentMetadataDTO to save.
     * @param documentMetadataDTO the documentMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the documentMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentMetadataDTO> updateDocumentMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentMetadataDTO documentMetadataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentMetadata : {}, {}", id, documentMetadataDTO);
        if (documentMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentMetadataDTO = documentMetadataService.update(documentMetadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentMetadataDTO.getId().toString()))
            .body(documentMetadataDTO);
    }

    /**
     * {@code PATCH  /document-metadata/:id} : Partial updates given fields of an existing documentMetadata, field will ignore if it is null
     *
     * @param id the id of the documentMetadataDTO to save.
     * @param documentMetadataDTO the documentMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the documentMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentMetadataDTO> partialUpdateDocumentMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentMetadataDTO documentMetadataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentMetadata partially : {}, {}", id, documentMetadataDTO);
        if (documentMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentMetadataDTO> result = documentMetadataService.partialUpdate(documentMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-metadata} : get all the documentMetadata.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentMetadata in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentMetadataDTO>> getAllDocumentMetadata(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentMetadata");
        Page<DocumentMetadataDTO> page = documentMetadataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-metadata/:id} : get the "id" documentMetadata.
     *
     * @param id the id of the documentMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentMetadataDTO> getDocumentMetadata(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentMetadata : {}", id);
        Optional<DocumentMetadataDTO> documentMetadataDTO = documentMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentMetadataDTO);
    }

    /**
     * {@code DELETE  /document-metadata/:id} : delete the "id" documentMetadata.
     *
     * @param id the id of the documentMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentMetadata(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentMetadata : {}", id);
        documentMetadataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
