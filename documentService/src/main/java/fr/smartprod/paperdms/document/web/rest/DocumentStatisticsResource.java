package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
import fr.smartprod.paperdms.document.service.DocumentStatisticsService;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentStatistics}.
 */
@RestController
@RequestMapping("/api/document-statistics")
public class DocumentStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentStatisticsResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentStatisticsService documentStatisticsService;

    private final DocumentStatisticsRepository documentStatisticsRepository;

    public DocumentStatisticsResource(
        DocumentStatisticsService documentStatisticsService,
        DocumentStatisticsRepository documentStatisticsRepository
    ) {
        this.documentStatisticsService = documentStatisticsService;
        this.documentStatisticsRepository = documentStatisticsRepository;
    }

    /**
     * {@code POST  /document-statistics} : Create a new documentStatistics.
     *
     * @param documentStatisticsDTO the documentStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentStatisticsDTO, or with status {@code 400 (Bad Request)} if the documentStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentStatisticsDTO> createDocumentStatistics(@Valid @RequestBody DocumentStatisticsDTO documentStatisticsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentStatistics : {}", documentStatisticsDTO);
        if (documentStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentStatisticsDTO = documentStatisticsService.save(documentStatisticsDTO);
        return ResponseEntity.created(new URI("/api/document-statistics/" + documentStatisticsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentStatisticsDTO.getId().toString()))
            .body(documentStatisticsDTO);
    }

    /**
     * {@code PUT  /document-statistics/:id} : Updates an existing documentStatistics.
     *
     * @param id the id of the documentStatisticsDTO to save.
     * @param documentStatisticsDTO the documentStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the documentStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentStatisticsDTO> updateDocumentStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentStatisticsDTO documentStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentStatistics : {}, {}", id, documentStatisticsDTO);
        if (documentStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentStatisticsDTO = documentStatisticsService.update(documentStatisticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentStatisticsDTO.getId().toString()))
            .body(documentStatisticsDTO);
    }

    /**
     * {@code PATCH  /document-statistics/:id} : Partial updates given fields of an existing documentStatistics, field will ignore if it is null
     *
     * @param id the id of the documentStatisticsDTO to save.
     * @param documentStatisticsDTO the documentStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the documentStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentStatisticsDTO> partialUpdateDocumentStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentStatisticsDTO documentStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentStatistics partially : {}, {}", id, documentStatisticsDTO);
        if (documentStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentStatisticsDTO> result = documentStatisticsService.partialUpdate(documentStatisticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentStatisticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-statistics} : get all the documentStatistics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentStatistics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentStatisticsDTO>> getAllDocumentStatistics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentStatistics");
        Page<DocumentStatisticsDTO> page = documentStatisticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-statistics/:id} : get the "id" documentStatistics.
     *
     * @param id the id of the documentStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentStatisticsDTO> getDocumentStatistics(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentStatistics : {}", id);
        Optional<DocumentStatisticsDTO> documentStatisticsDTO = documentStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentStatisticsDTO);
    }

    /**
     * {@code DELETE  /document-statistics/:id} : delete the "id" documentStatistics.
     *
     * @param id the id of the documentStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentStatistics(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentStatistics : {}", id);
        documentStatisticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
