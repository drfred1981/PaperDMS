package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.PerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.PerformanceMetricService;
import fr.smartprod.paperdms.reporting.service.dto.PerformanceMetricDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.PerformanceMetric}.
 */
@RestController
@RequestMapping("/api/performance-metrics")
public class PerformanceMetricResource {

    private static final Logger LOG = LoggerFactory.getLogger(PerformanceMetricResource.class);

    private static final String ENTITY_NAME = "reportingServicePerformanceMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerformanceMetricService performanceMetricService;

    private final PerformanceMetricRepository performanceMetricRepository;

    public PerformanceMetricResource(
        PerformanceMetricService performanceMetricService,
        PerformanceMetricRepository performanceMetricRepository
    ) {
        this.performanceMetricService = performanceMetricService;
        this.performanceMetricRepository = performanceMetricRepository;
    }

    /**
     * {@code POST  /performance-metrics} : Create a new performanceMetric.
     *
     * @param performanceMetricDTO the performanceMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new performanceMetricDTO, or with status {@code 400 (Bad Request)} if the performanceMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PerformanceMetricDTO> createPerformanceMetric(@Valid @RequestBody PerformanceMetricDTO performanceMetricDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PerformanceMetric : {}", performanceMetricDTO);
        if (performanceMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new performanceMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        performanceMetricDTO = performanceMetricService.save(performanceMetricDTO);
        return ResponseEntity.created(new URI("/api/performance-metrics/" + performanceMetricDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, performanceMetricDTO.getId().toString()))
            .body(performanceMetricDTO);
    }

    /**
     * {@code PUT  /performance-metrics/:id} : Updates an existing performanceMetric.
     *
     * @param id the id of the performanceMetricDTO to save.
     * @param performanceMetricDTO the performanceMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated performanceMetricDTO,
     * or with status {@code 400 (Bad Request)} if the performanceMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the performanceMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PerformanceMetricDTO> updatePerformanceMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerformanceMetricDTO performanceMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PerformanceMetric : {}, {}", id, performanceMetricDTO);
        if (performanceMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, performanceMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!performanceMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        performanceMetricDTO = performanceMetricService.update(performanceMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, performanceMetricDTO.getId().toString()))
            .body(performanceMetricDTO);
    }

    /**
     * {@code PATCH  /performance-metrics/:id} : Partial updates given fields of an existing performanceMetric, field will ignore if it is null
     *
     * @param id the id of the performanceMetricDTO to save.
     * @param performanceMetricDTO the performanceMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated performanceMetricDTO,
     * or with status {@code 400 (Bad Request)} if the performanceMetricDTO is not valid,
     * or with status {@code 404 (Not Found)} if the performanceMetricDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the performanceMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerformanceMetricDTO> partialUpdatePerformanceMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerformanceMetricDTO performanceMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PerformanceMetric partially : {}, {}", id, performanceMetricDTO);
        if (performanceMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, performanceMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!performanceMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerformanceMetricDTO> result = performanceMetricService.partialUpdate(performanceMetricDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, performanceMetricDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /performance-metrics} : get all the performanceMetrics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of performanceMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PerformanceMetricDTO>> getAllPerformanceMetrics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PerformanceMetrics");
        Page<PerformanceMetricDTO> page = performanceMetricService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /performance-metrics/:id} : get the "id" performanceMetric.
     *
     * @param id the id of the performanceMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the performanceMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceMetricDTO> getPerformanceMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PerformanceMetric : {}", id);
        Optional<PerformanceMetricDTO> performanceMetricDTO = performanceMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(performanceMetricDTO);
    }

    /**
     * {@code DELETE  /performance-metrics/:id} : delete the "id" performanceMetric.
     *
     * @param id the id of the performanceMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformanceMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PerformanceMetric : {}", id);
        performanceMetricService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
