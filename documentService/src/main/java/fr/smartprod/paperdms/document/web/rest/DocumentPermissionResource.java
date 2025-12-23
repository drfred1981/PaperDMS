package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
import fr.smartprod.paperdms.document.service.DocumentPermissionQueryService;
import fr.smartprod.paperdms.document.service.DocumentPermissionService;
import fr.smartprod.paperdms.document.service.criteria.DocumentPermissionCriteria;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentPermission}.
 */
@RestController
@RequestMapping("/api/document-permissions")
public class DocumentPermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentPermission";

    @Value("${jhipster.clientApp.name:documentService}")
    private String applicationName;

    private final DocumentPermissionService documentPermissionService;

    private final DocumentPermissionRepository documentPermissionRepository;

    private final DocumentPermissionQueryService documentPermissionQueryService;

    public DocumentPermissionResource(
        DocumentPermissionService documentPermissionService,
        DocumentPermissionRepository documentPermissionRepository,
        DocumentPermissionQueryService documentPermissionQueryService
    ) {
        this.documentPermissionService = documentPermissionService;
        this.documentPermissionRepository = documentPermissionRepository;
        this.documentPermissionQueryService = documentPermissionQueryService;
    }

    /**
     * {@code POST  /document-permissions} : Create a new documentPermission.
     *
     * @param documentPermissionDTO the documentPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentPermissionDTO, or with status {@code 400 (Bad Request)} if the documentPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentPermissionDTO> createDocumentPermission(@Valid @RequestBody DocumentPermissionDTO documentPermissionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentPermission : {}", documentPermissionDTO);
        if (documentPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentPermissionDTO = documentPermissionService.save(documentPermissionDTO);
        return ResponseEntity.created(new URI("/api/document-permissions/" + documentPermissionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentPermissionDTO.getId().toString()))
            .body(documentPermissionDTO);
    }

    /**
     * {@code PUT  /document-permissions/:id} : Updates an existing documentPermission.
     *
     * @param id the id of the documentPermissionDTO to save.
     * @param documentPermissionDTO the documentPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the documentPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentPermissionDTO> updateDocumentPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentPermissionDTO documentPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentPermission : {}, {}", id, documentPermissionDTO);
        if (documentPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentPermissionDTO = documentPermissionService.update(documentPermissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentPermissionDTO.getId().toString()))
            .body(documentPermissionDTO);
    }

    /**
     * {@code PATCH  /document-permissions/:id} : Partial updates given fields of an existing documentPermission, field will ignore if it is null
     *
     * @param id the id of the documentPermissionDTO to save.
     * @param documentPermissionDTO the documentPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the documentPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentPermissionDTO> partialUpdateDocumentPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentPermissionDTO documentPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentPermission partially : {}, {}", id, documentPermissionDTO);
        if (documentPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentPermissionDTO> result = documentPermissionService.partialUpdate(documentPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-permissions} : get all the documentPermissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentPermissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentPermissionDTO>> getAllDocumentPermissions(
        DocumentPermissionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentPermissions by criteria: {}", criteria);

        Page<DocumentPermissionDTO> page = documentPermissionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-permissions/count} : count all the documentPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentPermissions(DocumentPermissionCriteria criteria) {
        LOG.debug("REST request to count DocumentPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-permissions/:id} : get the "id" documentPermission.
     *
     * @param id the id of the documentPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentPermissionDTO> getDocumentPermission(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentPermission : {}", id);
        Optional<DocumentPermissionDTO> documentPermissionDTO = documentPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentPermissionDTO);
    }

    /**
     * {@code DELETE  /document-permissions/:id} : delete the "id" documentPermission.
     *
     * @param id the id of the documentPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentPermission(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentPermission : {}", id);
        documentPermissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
