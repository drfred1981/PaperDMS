package fr.smartprod.paperdms.emailimport.web.rest;

import fr.smartprod.paperdms.emailimport.repository.ImportMappingRepository;
import fr.smartprod.paperdms.emailimport.service.ImportMappingService;
import fr.smartprod.paperdms.emailimport.service.dto.ImportMappingDTO;
import fr.smartprod.paperdms.emailimport.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportMapping}.
 */
@RestController
@RequestMapping("/api/import-mappings")
public class ImportMappingResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImportMappingResource.class);

    private static final String ENTITY_NAME = "emailImportServiceImportMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImportMappingService importMappingService;

    private final ImportMappingRepository importMappingRepository;

    public ImportMappingResource(ImportMappingService importMappingService, ImportMappingRepository importMappingRepository) {
        this.importMappingService = importMappingService;
        this.importMappingRepository = importMappingRepository;
    }

    /**
     * {@code POST  /import-mappings} : Create a new importMapping.
     *
     * @param importMappingDTO the importMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new importMappingDTO, or with status {@code 400 (Bad Request)} if the importMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImportMappingDTO> createImportMapping(@Valid @RequestBody ImportMappingDTO importMappingDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ImportMapping : {}", importMappingDTO);
        if (importMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new importMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        importMappingDTO = importMappingService.save(importMappingDTO);
        return ResponseEntity.created(new URI("/api/import-mappings/" + importMappingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, importMappingDTO.getId().toString()))
            .body(importMappingDTO);
    }

    /**
     * {@code PUT  /import-mappings/:id} : Updates an existing importMapping.
     *
     * @param id the id of the importMappingDTO to save.
     * @param importMappingDTO the importMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importMappingDTO,
     * or with status {@code 400 (Bad Request)} if the importMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the importMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImportMappingDTO> updateImportMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImportMappingDTO importMappingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImportMapping : {}, {}", id, importMappingDTO);
        if (importMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, importMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!importMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        importMappingDTO = importMappingService.update(importMappingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, importMappingDTO.getId().toString()))
            .body(importMappingDTO);
    }

    /**
     * {@code PATCH  /import-mappings/:id} : Partial updates given fields of an existing importMapping, field will ignore if it is null
     *
     * @param id the id of the importMappingDTO to save.
     * @param importMappingDTO the importMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importMappingDTO,
     * or with status {@code 400 (Bad Request)} if the importMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the importMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the importMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImportMappingDTO> partialUpdateImportMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImportMappingDTO importMappingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImportMapping partially : {}, {}", id, importMappingDTO);
        if (importMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, importMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!importMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImportMappingDTO> result = importMappingService.partialUpdate(importMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, importMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /import-mappings} : get all the importMappings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importMappings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImportMappingDTO>> getAllImportMappings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ImportMappings");
        Page<ImportMappingDTO> page = importMappingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /import-mappings/:id} : get the "id" importMapping.
     *
     * @param id the id of the importMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the importMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImportMappingDTO> getImportMapping(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImportMapping : {}", id);
        Optional<ImportMappingDTO> importMappingDTO = importMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(importMappingDTO);
    }

    /**
     * {@code DELETE  /import-mappings/:id} : delete the "id" importMapping.
     *
     * @param id the id of the importMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportMapping(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImportMapping : {}", id);
        importMappingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
