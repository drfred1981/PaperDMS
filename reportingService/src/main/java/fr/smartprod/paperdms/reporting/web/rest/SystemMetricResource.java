package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.SystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.SystemMetricService;
import fr.smartprod.paperdms.reporting.service.dto.SystemMetricDTO;
import fr.smartprod.paperdms.reporting.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.SystemMetric}.
 */
@RestController
@RequestMapping("/api/system-metrics")
public class SystemMetricResource {

    private static final Logger LOG = LoggerFactory.getLogger(SystemMetricResource.class);

    private static final String ENTITY_NAME = "reportingServiceSystemMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemMetricService systemMetricService;

    private final SystemMetricRepository systemMetricRepository;

    public SystemMetricResource(SystemMetricService systemMetricService, SystemMetricRepository systemMetricRepository) {
        this.systemMetricService = systemMetricService;
        this.systemMetricRepository = systemMetricRepository;
    }

    /**
     * {@code POST  /system-metrics} : Create a new systemMetric.
     *
     * @param systemMetricDTO the systemMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemMetricDTO, or with status {@code 400 (Bad Request)} if the systemMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemMetricDTO> createSystemMetric(@Valid @RequestBody SystemMetricDTO systemMetricDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SystemMetric : {}", systemMetricDTO);
        if (systemMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemMetricDTO = systemMetricService.save(systemMetricDTO);
        return ResponseEntity.created(new URI("/api/system-metrics/" + systemMetricDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemMetricDTO.getId().toString()))
            .body(systemMetricDTO);
    }

    /**
     * {@code PUT  /system-metrics/:id} : Updates an existing systemMetric.
     *
     * @param id the id of the systemMetricDTO to save.
     * @param systemMetricDTO the systemMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemMetricDTO,
     * or with status {@code 400 (Bad Request)} if the systemMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemMetricDTO> updateSystemMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemMetricDTO systemMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SystemMetric : {}, {}", id, systemMetricDTO);
        if (systemMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemMetricDTO = systemMetricService.update(systemMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemMetricDTO.getId().toString()))
            .body(systemMetricDTO);
    }

    /**
     * {@code PATCH  /system-metrics/:id} : Partial updates given fields of an existing systemMetric, field will ignore if it is null
     *
     * @param id the id of the systemMetricDTO to save.
     * @param systemMetricDTO the systemMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemMetricDTO,
     * or with status {@code 400 (Bad Request)} if the systemMetricDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemMetricDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemMetricDTO> partialUpdateSystemMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemMetricDTO systemMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SystemMetric partially : {}, {}", id, systemMetricDTO);
        if (systemMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemMetricDTO> result = systemMetricService.partialUpdate(systemMetricDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemMetricDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-metrics} : get all the systemMetrics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemMetricDTO>> getAllSystemMetrics(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SystemMetrics");
        Page<SystemMetricDTO> page = systemMetricService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-metrics/:id} : get the "id" systemMetric.
     *
     * @param id the id of the systemMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemMetricDTO> getSystemMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SystemMetric : {}", id);
        Optional<SystemMetricDTO> systemMetricDTO = systemMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemMetricDTO);
    }

    /**
     * {@code DELETE  /system-metrics/:id} : delete the "id" systemMetric.
     *
     * @param id the id of the systemMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SystemMetric : {}", id);
        systemMetricService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
