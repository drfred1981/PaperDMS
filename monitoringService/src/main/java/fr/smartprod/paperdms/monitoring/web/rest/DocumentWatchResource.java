package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.DocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.DocumentWatchQueryService;
import fr.smartprod.paperdms.monitoring.service.DocumentWatchService;
import fr.smartprod.paperdms.monitoring.service.criteria.DocumentWatchCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.DocumentWatch}.
 */
@RestController
@RequestMapping("/api/document-watches")
public class DocumentWatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentWatchResource.class);

    private static final String ENTITY_NAME = "monitoringServiceDocumentWatch";

    @Value("${jhipster.clientApp.name:monitoringService}")
    private String applicationName;

    private final DocumentWatchService documentWatchService;

    private final DocumentWatchRepository documentWatchRepository;

    private final DocumentWatchQueryService documentWatchQueryService;

    public DocumentWatchResource(
        DocumentWatchService documentWatchService,
        DocumentWatchRepository documentWatchRepository,
        DocumentWatchQueryService documentWatchQueryService
    ) {
        this.documentWatchService = documentWatchService;
        this.documentWatchRepository = documentWatchRepository;
        this.documentWatchQueryService = documentWatchQueryService;
    }

    /**
     * {@code POST  /document-watches} : Create a new documentWatch.
     *
     * @param documentWatchDTO the documentWatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentWatchDTO, or with status {@code 400 (Bad Request)} if the documentWatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentWatchDTO> createDocumentWatch(@Valid @RequestBody DocumentWatchDTO documentWatchDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentWatch : {}", documentWatchDTO);
        if (documentWatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentWatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentWatchDTO = documentWatchService.save(documentWatchDTO);
        return ResponseEntity.created(new URI("/api/document-watches/" + documentWatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentWatchDTO.getId().toString()))
            .body(documentWatchDTO);
    }

    /**
     * {@code PUT  /document-watches/:id} : Updates an existing documentWatch.
     *
     * @param id the id of the documentWatchDTO to save.
     * @param documentWatchDTO the documentWatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentWatchDTO,
     * or with status {@code 400 (Bad Request)} if the documentWatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentWatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentWatchDTO> updateDocumentWatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentWatchDTO documentWatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentWatch : {}, {}", id, documentWatchDTO);
        if (documentWatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentWatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentWatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentWatchDTO = documentWatchService.update(documentWatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentWatchDTO.getId().toString()))
            .body(documentWatchDTO);
    }

    /**
     * {@code PATCH  /document-watches/:id} : Partial updates given fields of an existing documentWatch, field will ignore if it is null
     *
     * @param id the id of the documentWatchDTO to save.
     * @param documentWatchDTO the documentWatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentWatchDTO,
     * or with status {@code 400 (Bad Request)} if the documentWatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentWatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentWatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentWatchDTO> partialUpdateDocumentWatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentWatchDTO documentWatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentWatch partially : {}, {}", id, documentWatchDTO);
        if (documentWatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentWatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentWatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentWatchDTO> result = documentWatchService.partialUpdate(documentWatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentWatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-watches} : get all the documentWatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentWatches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentWatchDTO>> getAllDocumentWatches(
        DocumentWatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentWatches by criteria: {}", criteria);

        Page<DocumentWatchDTO> page = documentWatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-watches/count} : count all the documentWatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentWatches(DocumentWatchCriteria criteria) {
        LOG.debug("REST request to count DocumentWatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentWatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-watches/:id} : get the "id" documentWatch.
     *
     * @param id the id of the documentWatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentWatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentWatchDTO> getDocumentWatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentWatch : {}", id);
        Optional<DocumentWatchDTO> documentWatchDTO = documentWatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentWatchDTO);
    }

    /**
     * {@code DELETE  /document-watches/:id} : delete the "id" documentWatch.
     *
     * @param id the id of the documentWatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentWatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentWatch : {}", id);
        documentWatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
