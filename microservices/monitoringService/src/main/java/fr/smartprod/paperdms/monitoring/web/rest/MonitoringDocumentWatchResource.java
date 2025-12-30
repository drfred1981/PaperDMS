package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringDocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringDocumentWatchQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringDocumentWatchService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringDocumentWatchCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringDocumentWatchDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch}.
 */
@RestController
@RequestMapping("/api/monitoring-document-watches")
public class MonitoringDocumentWatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringDocumentWatchResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringDocumentWatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringDocumentWatchService monitoringDocumentWatchService;

    private final MonitoringDocumentWatchRepository monitoringDocumentWatchRepository;

    private final MonitoringDocumentWatchQueryService monitoringDocumentWatchQueryService;

    public MonitoringDocumentWatchResource(
        MonitoringDocumentWatchService monitoringDocumentWatchService,
        MonitoringDocumentWatchRepository monitoringDocumentWatchRepository,
        MonitoringDocumentWatchQueryService monitoringDocumentWatchQueryService
    ) {
        this.monitoringDocumentWatchService = monitoringDocumentWatchService;
        this.monitoringDocumentWatchRepository = monitoringDocumentWatchRepository;
        this.monitoringDocumentWatchQueryService = monitoringDocumentWatchQueryService;
    }

    /**
     * {@code POST  /monitoring-document-watches} : Create a new monitoringDocumentWatch.
     *
     * @param monitoringDocumentWatchDTO the monitoringDocumentWatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringDocumentWatchDTO, or with status {@code 400 (Bad Request)} if the monitoringDocumentWatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringDocumentWatchDTO> createMonitoringDocumentWatch(
        @Valid @RequestBody MonitoringDocumentWatchDTO monitoringDocumentWatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MonitoringDocumentWatch : {}", monitoringDocumentWatchDTO);
        if (monitoringDocumentWatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringDocumentWatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringDocumentWatchDTO = monitoringDocumentWatchService.save(monitoringDocumentWatchDTO);
        return ResponseEntity.created(new URI("/api/monitoring-document-watches/" + monitoringDocumentWatchDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringDocumentWatchDTO.getId().toString())
            )
            .body(monitoringDocumentWatchDTO);
    }

    /**
     * {@code PUT  /monitoring-document-watches/:id} : Updates an existing monitoringDocumentWatch.
     *
     * @param id the id of the monitoringDocumentWatchDTO to save.
     * @param monitoringDocumentWatchDTO the monitoringDocumentWatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringDocumentWatchDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringDocumentWatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringDocumentWatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringDocumentWatchDTO> updateMonitoringDocumentWatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringDocumentWatchDTO monitoringDocumentWatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringDocumentWatch : {}, {}", id, monitoringDocumentWatchDTO);
        if (monitoringDocumentWatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringDocumentWatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringDocumentWatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringDocumentWatchDTO = monitoringDocumentWatchService.update(monitoringDocumentWatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringDocumentWatchDTO.getId().toString()))
            .body(monitoringDocumentWatchDTO);
    }

    /**
     * {@code PATCH  /monitoring-document-watches/:id} : Partial updates given fields of an existing monitoringDocumentWatch, field will ignore if it is null
     *
     * @param id the id of the monitoringDocumentWatchDTO to save.
     * @param monitoringDocumentWatchDTO the monitoringDocumentWatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringDocumentWatchDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringDocumentWatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringDocumentWatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringDocumentWatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringDocumentWatchDTO> partialUpdateMonitoringDocumentWatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringDocumentWatchDTO monitoringDocumentWatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringDocumentWatch partially : {}, {}", id, monitoringDocumentWatchDTO);
        if (monitoringDocumentWatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringDocumentWatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringDocumentWatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringDocumentWatchDTO> result = monitoringDocumentWatchService.partialUpdate(monitoringDocumentWatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringDocumentWatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-document-watches} : get all the monitoringDocumentWatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringDocumentWatches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringDocumentWatchDTO>> getAllMonitoringDocumentWatches(
        MonitoringDocumentWatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringDocumentWatches by criteria: {}", criteria);

        Page<MonitoringDocumentWatchDTO> page = monitoringDocumentWatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-document-watches/count} : count all the monitoringDocumentWatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringDocumentWatches(MonitoringDocumentWatchCriteria criteria) {
        LOG.debug("REST request to count MonitoringDocumentWatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringDocumentWatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-document-watches/:id} : get the "id" monitoringDocumentWatch.
     *
     * @param id the id of the monitoringDocumentWatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringDocumentWatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringDocumentWatchDTO> getMonitoringDocumentWatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringDocumentWatch : {}", id);
        Optional<MonitoringDocumentWatchDTO> monitoringDocumentWatchDTO = monitoringDocumentWatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringDocumentWatchDTO);
    }

    /**
     * {@code DELETE  /monitoring-document-watches/:id} : delete the "id" monitoringDocumentWatch.
     *
     * @param id the id of the monitoringDocumentWatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringDocumentWatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringDocumentWatch : {}", id);
        monitoringDocumentWatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
