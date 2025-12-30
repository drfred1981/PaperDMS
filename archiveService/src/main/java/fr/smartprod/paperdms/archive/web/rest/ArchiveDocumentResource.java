package fr.smartprod.paperdms.archive.web.rest;

import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.ArchiveDocumentQueryService;
import fr.smartprod.paperdms.archive.service.ArchiveDocumentService;
import fr.smartprod.paperdms.archive.service.criteria.ArchiveDocumentCriteria;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument}.
 */
@RestController
@RequestMapping("/api/archive-documents")
public class ArchiveDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveDocumentResource.class);

    private static final String ENTITY_NAME = "archiveServiceArchiveDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArchiveDocumentService archiveDocumentService;

    private final ArchiveDocumentRepository archiveDocumentRepository;

    private final ArchiveDocumentQueryService archiveDocumentQueryService;

    public ArchiveDocumentResource(
        ArchiveDocumentService archiveDocumentService,
        ArchiveDocumentRepository archiveDocumentRepository,
        ArchiveDocumentQueryService archiveDocumentQueryService
    ) {
        this.archiveDocumentService = archiveDocumentService;
        this.archiveDocumentRepository = archiveDocumentRepository;
        this.archiveDocumentQueryService = archiveDocumentQueryService;
    }

    /**
     * {@code POST  /archive-documents} : Create a new archiveDocument.
     *
     * @param archiveDocumentDTO the archiveDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archiveDocumentDTO, or with status {@code 400 (Bad Request)} if the archiveDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArchiveDocumentDTO> createArchiveDocument(@Valid @RequestBody ArchiveDocumentDTO archiveDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ArchiveDocument : {}", archiveDocumentDTO);
        if (archiveDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new archiveDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        archiveDocumentDTO = archiveDocumentService.save(archiveDocumentDTO);
        return ResponseEntity.created(new URI("/api/archive-documents/" + archiveDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, archiveDocumentDTO.getId().toString()))
            .body(archiveDocumentDTO);
    }

    /**
     * {@code PUT  /archive-documents/:id} : Updates an existing archiveDocument.
     *
     * @param id the id of the archiveDocumentDTO to save.
     * @param archiveDocumentDTO the archiveDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the archiveDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archiveDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArchiveDocumentDTO> updateArchiveDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArchiveDocumentDTO archiveDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArchiveDocument : {}, {}", id, archiveDocumentDTO);
        if (archiveDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        archiveDocumentDTO = archiveDocumentService.update(archiveDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveDocumentDTO.getId().toString()))
            .body(archiveDocumentDTO);
    }

    /**
     * {@code PATCH  /archive-documents/:id} : Partial updates given fields of an existing archiveDocument, field will ignore if it is null
     *
     * @param id the id of the archiveDocumentDTO to save.
     * @param archiveDocumentDTO the archiveDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the archiveDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the archiveDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the archiveDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArchiveDocumentDTO> partialUpdateArchiveDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArchiveDocumentDTO archiveDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArchiveDocument partially : {}, {}", id, archiveDocumentDTO);
        if (archiveDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArchiveDocumentDTO> result = archiveDocumentService.partialUpdate(archiveDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /archive-documents} : get all the archiveDocuments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archiveDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArchiveDocumentDTO>> getAllArchiveDocuments(
        ArchiveDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ArchiveDocuments by criteria: {}", criteria);

        Page<ArchiveDocumentDTO> page = archiveDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /archive-documents/count} : count all the archiveDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArchiveDocuments(ArchiveDocumentCriteria criteria) {
        LOG.debug("REST request to count ArchiveDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(archiveDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /archive-documents/:id} : get the "id" archiveDocument.
     *
     * @param id the id of the archiveDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archiveDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArchiveDocumentDTO> getArchiveDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArchiveDocument : {}", id);
        Optional<ArchiveDocumentDTO> archiveDocumentDTO = archiveDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(archiveDocumentDTO);
    }

    /**
     * {@code DELETE  /archive-documents/:id} : delete the "id" archiveDocument.
     *
     * @param id the id of the archiveDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchiveDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArchiveDocument : {}", id);
        archiveDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
