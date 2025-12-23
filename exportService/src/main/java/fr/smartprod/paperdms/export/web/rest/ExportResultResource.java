package fr.smartprod.paperdms.export.web.rest;

import fr.smartprod.paperdms.export.repository.ExportResultRepository;
import fr.smartprod.paperdms.export.service.ExportResultService;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import fr.smartprod.paperdms.export.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.export.domain.ExportResult}.
 */
@RestController
@RequestMapping("/api/export-results")
public class ExportResultResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExportResultResource.class);

    private static final String ENTITY_NAME = "exportServiceExportResult";

    @Value("${jhipster.clientApp.name:exportService}")
    private String applicationName;

    private final ExportResultService exportResultService;

    private final ExportResultRepository exportResultRepository;

    public ExportResultResource(ExportResultService exportResultService, ExportResultRepository exportResultRepository) {
        this.exportResultService = exportResultService;
        this.exportResultRepository = exportResultRepository;
    }

    /**
     * {@code POST  /export-results} : Create a new exportResult.
     *
     * @param exportResultDTO the exportResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exportResultDTO, or with status {@code 400 (Bad Request)} if the exportResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExportResultDTO> createExportResult(@Valid @RequestBody ExportResultDTO exportResultDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExportResult : {}", exportResultDTO);
        if (exportResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new exportResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        exportResultDTO = exportResultService.save(exportResultDTO);
        return ResponseEntity.created(new URI("/api/export-results/" + exportResultDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, exportResultDTO.getId().toString()))
            .body(exportResultDTO);
    }

    /**
     * {@code PUT  /export-results/:id} : Updates an existing exportResult.
     *
     * @param id the id of the exportResultDTO to save.
     * @param exportResultDTO the exportResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportResultDTO,
     * or with status {@code 400 (Bad Request)} if the exportResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exportResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExportResultDTO> updateExportResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExportResultDTO exportResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExportResult : {}, {}", id, exportResultDTO);
        if (exportResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        exportResultDTO = exportResultService.update(exportResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportResultDTO.getId().toString()))
            .body(exportResultDTO);
    }

    /**
     * {@code PATCH  /export-results/:id} : Partial updates given fields of an existing exportResult, field will ignore if it is null
     *
     * @param id the id of the exportResultDTO to save.
     * @param exportResultDTO the exportResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportResultDTO,
     * or with status {@code 400 (Bad Request)} if the exportResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exportResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exportResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExportResultDTO> partialUpdateExportResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExportResultDTO exportResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExportResult partially : {}, {}", id, exportResultDTO);
        if (exportResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExportResultDTO> result = exportResultService.partialUpdate(exportResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /export-results} : get all the exportResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exportResults in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExportResultDTO>> getAllExportResults(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ExportResults");
        Page<ExportResultDTO> page = exportResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /export-results/:id} : get the "id" exportResult.
     *
     * @param id the id of the exportResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exportResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExportResultDTO> getExportResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExportResult : {}", id);
        Optional<ExportResultDTO> exportResultDTO = exportResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exportResultDTO);
    }

    /**
     * {@code DELETE  /export-results/:id} : delete the "id" exportResult.
     *
     * @param id the id of the exportResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExportResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExportResult : {}", id);
        exportResultService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
