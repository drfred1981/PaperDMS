package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.service.DocumentTypeFieldService;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.DocumentTypeField}.
 */
@RestController
@RequestMapping("/api/document-type-fields")
public class DocumentTypeFieldResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeFieldResource.class);

    private static final String ENTITY_NAME = "documentServiceDocumentTypeField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTypeFieldService documentTypeFieldService;

    private final DocumentTypeFieldRepository documentTypeFieldRepository;

    public DocumentTypeFieldResource(
        DocumentTypeFieldService documentTypeFieldService,
        DocumentTypeFieldRepository documentTypeFieldRepository
    ) {
        this.documentTypeFieldService = documentTypeFieldService;
        this.documentTypeFieldRepository = documentTypeFieldRepository;
    }

    /**
     * {@code POST  /document-type-fields} : Create a new documentTypeField.
     *
     * @param documentTypeFieldDTO the documentTypeFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentTypeFieldDTO, or with status {@code 400 (Bad Request)} if the documentTypeField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentTypeFieldDTO> createDocumentTypeField(@Valid @RequestBody DocumentTypeFieldDTO documentTypeFieldDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentTypeField : {}", documentTypeFieldDTO);
        if (documentTypeFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentTypeField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentTypeFieldDTO = documentTypeFieldService.save(documentTypeFieldDTO);
        return ResponseEntity.created(new URI("/api/document-type-fields/" + documentTypeFieldDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentTypeFieldDTO.getId().toString()))
            .body(documentTypeFieldDTO);
    }

    /**
     * {@code PUT  /document-type-fields/:id} : Updates an existing documentTypeField.
     *
     * @param id the id of the documentTypeFieldDTO to save.
     * @param documentTypeFieldDTO the documentTypeFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTypeFieldDTO,
     * or with status {@code 400 (Bad Request)} if the documentTypeFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentTypeFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTypeFieldDTO> updateDocumentTypeField(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentTypeFieldDTO documentTypeFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentTypeField : {}, {}", id, documentTypeFieldDTO);
        if (documentTypeFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTypeFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTypeFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentTypeFieldDTO = documentTypeFieldService.update(documentTypeFieldDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTypeFieldDTO.getId().toString()))
            .body(documentTypeFieldDTO);
    }

    /**
     * {@code PATCH  /document-type-fields/:id} : Partial updates given fields of an existing documentTypeField, field will ignore if it is null
     *
     * @param id the id of the documentTypeFieldDTO to save.
     * @param documentTypeFieldDTO the documentTypeFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTypeFieldDTO,
     * or with status {@code 400 (Bad Request)} if the documentTypeFieldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentTypeFieldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentTypeFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentTypeFieldDTO> partialUpdateDocumentTypeField(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentTypeFieldDTO documentTypeFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentTypeField partially : {}, {}", id, documentTypeFieldDTO);
        if (documentTypeFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTypeFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTypeFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentTypeFieldDTO> result = documentTypeFieldService.partialUpdate(documentTypeFieldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTypeFieldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-type-fields} : get all the documentTypeFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTypeFields in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentTypeFieldDTO>> getAllDocumentTypeFields(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentTypeFields");
        Page<DocumentTypeFieldDTO> page = documentTypeFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-type-fields/:id} : get the "id" documentTypeField.
     *
     * @param id the id of the documentTypeFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentTypeFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeFieldDTO> getDocumentTypeField(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentTypeField : {}", id);
        Optional<DocumentTypeFieldDTO> documentTypeFieldDTO = documentTypeFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentTypeFieldDTO);
    }

    /**
     * {@code DELETE  /document-type-fields/:id} : delete the "id" documentTypeField.
     *
     * @param id the id of the documentTypeFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentTypeField(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentTypeField : {}", id);
        documentTypeFieldService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
