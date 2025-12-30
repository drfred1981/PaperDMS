package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringAlertQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringAlertService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringAlertCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlert}.
 */
@RestController
@RequestMapping("/api/monitoring-alerts")
public class MonitoringAlertResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringAlertService monitoringAlertService;

    private final MonitoringAlertRepository monitoringAlertRepository;

    private final MonitoringAlertQueryService monitoringAlertQueryService;

    public MonitoringAlertResource(
        MonitoringAlertService monitoringAlertService,
        MonitoringAlertRepository monitoringAlertRepository,
        MonitoringAlertQueryService monitoringAlertQueryService
    ) {
        this.monitoringAlertService = monitoringAlertService;
        this.monitoringAlertRepository = monitoringAlertRepository;
        this.monitoringAlertQueryService = monitoringAlertQueryService;
    }

    /**
     * {@code POST  /monitoring-alerts} : Create a new monitoringAlert.
     *
     * @param monitoringAlertDTO the monitoringAlertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringAlertDTO, or with status {@code 400 (Bad Request)} if the monitoringAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringAlertDTO> createMonitoringAlert(@Valid @RequestBody MonitoringAlertDTO monitoringAlertDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MonitoringAlert : {}", monitoringAlertDTO);
        if (monitoringAlertDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringAlertDTO = monitoringAlertService.save(monitoringAlertDTO);
        return ResponseEntity.created(new URI("/api/monitoring-alerts/" + monitoringAlertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringAlertDTO.getId().toString()))
            .body(monitoringAlertDTO);
    }

    /**
     * {@code PUT  /monitoring-alerts/:id} : Updates an existing monitoringAlert.
     *
     * @param id the id of the monitoringAlertDTO to save.
     * @param monitoringAlertDTO the monitoringAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringAlertDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringAlertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringAlertDTO> updateMonitoringAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringAlertDTO monitoringAlertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringAlert : {}, {}", id, monitoringAlertDTO);
        if (monitoringAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringAlertDTO = monitoringAlertService.update(monitoringAlertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringAlertDTO.getId().toString()))
            .body(monitoringAlertDTO);
    }

    /**
     * {@code PATCH  /monitoring-alerts/:id} : Partial updates given fields of an existing monitoringAlert, field will ignore if it is null
     *
     * @param id the id of the monitoringAlertDTO to save.
     * @param monitoringAlertDTO the monitoringAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringAlertDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringAlertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringAlertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringAlertDTO> partialUpdateMonitoringAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringAlertDTO monitoringAlertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringAlert partially : {}, {}", id, monitoringAlertDTO);
        if (monitoringAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringAlertDTO> result = monitoringAlertService.partialUpdate(monitoringAlertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringAlertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-alerts} : get all the monitoringAlerts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringAlerts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringAlertDTO>> getAllMonitoringAlerts(
        MonitoringAlertCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringAlerts by criteria: {}", criteria);

        Page<MonitoringAlertDTO> page = monitoringAlertQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-alerts/count} : count all the monitoringAlerts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringAlerts(MonitoringAlertCriteria criteria) {
        LOG.debug("REST request to count MonitoringAlerts by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringAlertQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-alerts/:id} : get the "id" monitoringAlert.
     *
     * @param id the id of the monitoringAlertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringAlertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringAlertDTO> getMonitoringAlert(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringAlert : {}", id);
        Optional<MonitoringAlertDTO> monitoringAlertDTO = monitoringAlertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringAlertDTO);
    }

    /**
     * {@code DELETE  /monitoring-alerts/:id} : delete the "id" monitoringAlert.
     *
     * @param id the id of the monitoringAlertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringAlert(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringAlert : {}", id);
        monitoringAlertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
